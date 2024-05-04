package com.jacobo.adyd.login.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jacobo.adyd.login.model.UserTable;

public interface UserTableRepository extends JpaRepository<UserTable, String> {
	
	public List<UserTable> findByToken(String token);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying
	@Query("update UserTable u set u.enabled=true, u.token=null where u.user = :usuario")
	public void enable(String usuario);
	
}
