package com.jacobo.adyd.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoginApi {

	public static void main(String[] args) {
		SpringApplication.run(LoginApi.class, args);
	}

}
