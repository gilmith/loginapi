package com.jacobo.adyd.login.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jacobo.adyd.login.exceptions.LoginException;
import com.jacobo.adyd.login.mail.MailInterface;
import com.jacobo.adyd.login.model.MailInput;
import com.jacobo.adyd.login.model.UserRecord;
import com.jacobo.adyd.login.model.UserTable;
import com.jacobo.adyd.login.repository.UserTableRepository;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	private final UserTableRepository repo;
	private final MailInterface mail;
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public Boolean isValid(UserRecord usuario) {
		return repo.findById(usuario.usuario()).map(user -> {
			return passwordEncoder.matches(usuario.password(), user.getPassword()) && user.getEnabled();
		}).get();
	}

	@Override
    @CircuitBreaker(name = "dbService", fallbackMethod = "fallbackMethod")
	public void createuser(UserRecord usuario) {
		val user = UserTable.builder()
				.user(usuario.usuario())
				.password(passwordEncoder.encode(usuario.password()))
				.enabled(false)
				.expriyDate(new Date().getTime() + TimeUnit.MINUTES.toMillis(10))
				.token(UUID.randomUUID().toString())
				.build();
			repo.findById(usuario.usuario()).ifPresent((it) -> {
				throw new LoginException("usuario ya creado", HttpStatus.BAD_REQUEST);
			});				

			repo.save(user);
			mail.createUser(MailInput.builder().para(user.getUser()).token(user.getToken()).build());

	}

    public void fallbackMethod(UserRecord usuario, Throwable t) throws Throwable {
    	if(t instanceof LoginException) {
    		log.info("ha pillado la exception abre el circuito pero no lanza nada especial ");
    	}
    		log.info("Datos predeterminados debido a un fallo en la base de datos " + t.getMessage());        
    		throw t;    	        
    }
    
    
	@Override
	public String checkMail(String mail) {
		return repo.findById(mail).map(it -> {
			return it.getUser();	
		}).orElse(null);
	}

	@Override
	public Boolean checkToken(String token) {
		val resultado = repo.findByToken(token);
		if(resultado.isEmpty()) return false;
		resultado.stream().filter(it -> it.getToken().equals(token)).findFirst().map(entrada -> {
			val date = new Date().getTime();
			if(date > entrada.getExpriyDate()) return false; 
			repo.enable(entrada.getUser());
			return true;			
		});
		return false;
	}

}

