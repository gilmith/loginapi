package com.jacobo.adyd.login;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CustomConfigEurekaClient {

	@Value("${trust.store:keystore/springboot.p12}")
	private ClassPathResource trustStore;

	@Value("${trust.store.password:password}")
	private String trustStorePassword;

	@Autowired
	private EurekaClientHttpRequestFactorySupplier requestFactory;

	@Bean
	public RestTemplateDiscoveryClientOptionalArgs getTrustStoredEurekaClient() throws KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		RestTemplateDiscoveryClientOptionalArgs args = new RestTemplateDiscoveryClientOptionalArgs(requestFactory);
		args.setSSLContext(new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build());
		args.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
		return args;
	}

}
