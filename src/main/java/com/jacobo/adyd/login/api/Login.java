package com.jacobo.adyd.login.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jacobo.adyd.login.model.Authorize;
import com.jacobo.adyd.login.model.UserRecord;

import jakarta.validation.Valid;
@Validated
public interface Login {
	
	
	@PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Authorize> auth(@Valid @RequestBody UserRecord usuario);
	
	@PostMapping(path = "login/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity create(@Validated @RequestBody UserRecord usuario);
	
	@GetMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserRecord> checkMail(@RequestParam String email);
	
	
	@GetMapping(path="login/confirm/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserRecord> confirmMail(@PathVariable String token);
}
