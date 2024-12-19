package com.jacobo.adyd.login.mail;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jacobo.adyd.login.model.MailInput;
import com.jacobo.adyd.login.model.ResponseMail;
@RefreshScope
@FeignClient(name = "maiClient", url = "${urlMail}", configuration = FeignConfig.class)
public interface MailInterface {

	@PostMapping(path = "${pathMail}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseMail createUser(@RequestBody MailInput id);

}
