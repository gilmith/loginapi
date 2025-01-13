package com.jacobo.adyd.login.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
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
@RefreshScope
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
				throw new LoginException("usuario ya creado", HttpStatus.ALREADY_REPORTED);
			});		
			repo.save(user);
			mail.createUser(MailInput.builder().para(user.getUser()).token(user.getToken()).build());

	}

    public void fallbackMethod(UserRecord usuario, Exception t) throws Throwable {
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
		val atomicBoolean = new AtomicBoolean(false);
		if(resultado.isEmpty()) {
			log.error("Error no se encuentra el token {}", token);
			return atomicBoolean.get();
		}
		resultado.stream().filter(it -> it.getToken().equals(token)).findFirst().map(entrada -> {
			val date = new Date().getTime();
			if(date > entrada.getExpriyDate()) {
				log.error("token {} caducado {}", token, date);
				return atomicBoolean.get(); 
			}
			log.info("habilita el usuario {}", entrada.getUser());
			repo.enable(entrada.getUser());
			atomicBoolean.set(true);
			return atomicBoolean.get();			
		});
		return atomicBoolean.get();
	}

	@Override
	public boolean reset1(String email) {
		val token = UUID.randomUUID().toString();
		repo.disableAndToken(email, token);
		mail.createUser(MailInput.builder().para(email).token(token).build());	
		return false;
	}

}

