package com.jacobo.adyd.login.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Users", schema = "ADYD_TABLAS",
uniqueConstraints = {
		@UniqueConstraint(columnNames = "USERNAME")
})
public class UserTable {
	
	@Id
	@Column(name="USERNAME", unique = true, updatable = false)
	private String user;
	@Column(name="PASSWORD")
	private String password;
	@Column(name = "ENABLED")
	private Boolean enabled;
	@Column(name = "EXPIRYDATE")
	private Long expriyDate;
	@Column(name = "TOKEN")
	private String token;
	@Column(name= "FECHA_CREACION")
	private LocalDateTime fechaCreacion;
	
	@PrePersist
	 public void prePersist() {
        if (fechaCreacion == null) {
        	fechaCreacion = LocalDateTime.now();
        }
    }


}
