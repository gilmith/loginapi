package com.jacobo.adyd.login.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jacobo.adyd.login.model.UserRecord;
import com.jacobo.adyd.login.model.UserTable;
import com.jacobo.adyd.login.repository.UserTableRepository;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	
	private final UserTableRepository repo;
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public Boolean isValid(UserRecord usuario) {
		return repo.findById(usuario.usuario()).map(user -> {
			return passwordEncoder.matches(usuario.password(), user.getPassword()) && user.getEnabled();
		}).get();
	}

	@Override
	public void createuser(UserRecord usuario) {
		val user = UserTable.builder()
				.user(usuario.usuario())
				.password(passwordEncoder.encode(usuario.password()))
				.enabled(false)
				.expriyDate(new Date().getTime() + TimeUnit.MINUTES.toMillis(10))
				.token(UUID.randomUUID().toString())
				.build();
		repo.save(user);		
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

