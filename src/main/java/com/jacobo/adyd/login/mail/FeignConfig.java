package com.jacobo.adyd.login.mail;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import feign.Client;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FeignConfig {

	@Value("${trust.store:keystore/springboot.p12}")
	private ClassPathResource trustStore;

	@Value("${trust.store.password:password}")
	private String trustStorePassword;

	@Bean
	public Client feignClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		val sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
		Client trustSSLSockets = new Client.Default(sslContext.getSocketFactory(), new NoopHostnameVerifier());
		return trustSSLSockets;
	}

}
