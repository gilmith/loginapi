package com.jacobo.adyd.login.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.jacobo.adyd.login.auth.TokenService;
import com.jacobo.adyd.login.model.Authorize;
import com.jacobo.adyd.login.model.UserRecord;
import com.jacobo.adyd.login.service.LoginService;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController implements Login {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private TokenService token;
	
	@Override
	public ResponseEntity<Authorize> auth(UserRecord usuario) {
		if(loginService.isValid(usuario)) {			
			return ResponseEntity.
					ok(Authorize.builder()
							.user(usuario.usuario())
							.accessToken(token.getToken(usuario.usuario()))
							.build()
					);
		}
		return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
	}

	@Override
	public ResponseEntity create(UserRecord usuario) {
		log.info("creacion el usuario con mail {}, ", usuario.usuario());
		loginService.createuser(usuario);
		return ResponseEntity.accepted().build();
	}

	@Override
	public ResponseEntity<UserRecord> checkMail(String email) {
		val mail = loginService.checkMail(email);
		if(Strings.isNullOrEmpty(mail)) {
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.ok(new UserRecord(mail, null));
	}

	@Override
	public ResponseEntity<UserRecord> confirmMail(String token) {
		log.info("esta confirmandose el token {}", token);		
		loginService.checkToken(token);
		return null;
	}
	

	
	

}
