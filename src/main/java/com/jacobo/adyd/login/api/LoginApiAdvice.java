package com.jacobo.adyd.login.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jacobo.adyd.login.exceptions.LoginException;
import com.jacobo.adyd.login.model.Confirm;
import com.jacobo.adyd.login.model.ErrorModel;

import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class LoginApiAdvice {
	
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ErrorModel> handleCircuitBreakerOpen(RetryableException ex) {
    	log.error("Lanza el 503, error de servicio de mail no disponible");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorModel("Error de registro por favor reintente", 2000L));
    }
    
    @ExceptionHandler(CallNotPermittedException.class)
    public  ResponseEntity<ErrorModel> handleCircuitBreakerOpen(CallNotPermittedException ex) {
    	log.error("circuito abierto");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorModel("Servicio de registro no disponible intentlo mas tarde", 2001L));
    }
    
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Confirm> handleLoginException(LoginException loginex){
    	log.error("Error de logado {}", loginex.getMessage());
    	return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(loginex.getConfirm());
    }

}
