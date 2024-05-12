package com.jacobo.adyd.login.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenServiceImpl implements TokenService{
	
	private Algorithm encodingAlgorithm;

	
	public TokenServiceImpl( @Value("${secret}") String secret) {
		encodingAlgorithm = Algorithm.HMAC384(secret);
	}
	

	@Override
	public String getToken(String user) {
		 return JWT.create()
	                .withIssuer("BACK")
	                .withSubject(user)	                
	                .withIssuedAt(Date.from(Instant.now()))
	                .withExpiresAt(Date.from(Instant.now().plus(Duration.ofHours(1L))))
	                .sign(encodingAlgorithm);
	}

}
