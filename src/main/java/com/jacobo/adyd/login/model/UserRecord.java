package com.jacobo.adyd.login.model;

import com.jacobo.adyd.login.validators.EmailValidator;
import com.jacobo.adyd.login.validators.NullValidator;
import com.jacobo.adyd.login.validators.PatternValidator;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@GroupSequence(value = {UserRecord.class, NullValidator.class, EmailValidator.class, PatternValidator.class})
public record UserRecord(
	@NotEmpty(message = "usuario vacio", groups = NullValidator.class)
	@Email(message = "no tiene un formato email", groups = EmailValidator.class)
	String usuario,
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "no cumple la expresion regular"
	, groups = PatternValidator.class)
	String password) {}


