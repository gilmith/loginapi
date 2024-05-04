package com.jacobo.adyd.login.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jacobo.adyd.login.model.UserRecord;

public interface LoginService {
	
	public Boolean isValid(UserRecord usuario);
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void createuser(UserRecord usuario);
	
	public String checkMail(String mail);

	public Boolean checkToken(String token);

}
