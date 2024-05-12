package com.jacobo.adyd.login.validators;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
@Validated
public interface ValidatedUsuario {
	@NotNull
	@Email
	String usuario();
	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
	String password();
}
