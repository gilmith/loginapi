package com.jacobo.adyd.login.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jacobo.adyd.login.exceptions.LoginException;

import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class LoginApiAdvice {
	
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleCircuitBreakerOpen(RetryableException ex) {
    	log.error("Lanza el 503");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }
    
    @ExceptionHandler(CallNotPermittedException.class)
    public  ResponseEntity<String> handleCircuitBreakerOpen(CallNotPermittedException ex) {
    	log.error("circuito abierto");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

}
