package com.jacobo.adyd.login.auth;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

public class TokenValidityException extends RuntimeException {

	public TokenValidityException(AlgorithmMismatchException e) {
		// TODO Auto-generated constructor stub
	}

	public TokenValidityException(SignatureVerificationException e) {
		// TODO Auto-generated constructor stub
	}

	public TokenValidityException(InvalidClaimException e) {
		// TODO Auto-generated constructor stub
	}

	public TokenValidityException(JWTDecodeException e) {
		// TODO Auto-generated constructor stub
	}

	public TokenValidityException(TokenExpiredException e) {
		// TODO Auto-generated constructor stub
	}

}
