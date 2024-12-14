package com.jacobo.adyd.login.mail;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.jacobo.adyd.login.exceptions.LoginException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(String methodKey, Response response) {
		log.error("valor de methodKey {}, valor de response code {}", methodKey
				, response.status());
		return new LoginException(response.body().toString(), HttpStatus.valueOf(response.status()));
	}

}
