package com.jacobo.adyd.login.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Users")
public class UserTable {
	
	@Id
	@Column(name="USERNAME")
	private String user;
	@Column(name="PASSWORD")
	private String password;
	@Column(name = "ENABLED")
	private Boolean enabled;
	@Column(name = "EXPIRYDATE")
	private Long expriyDate;
	@Column(name = "TOKEN")
	private String token;
	

}
