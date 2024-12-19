package com.jacobo.adyd.login.config;

import java.time.Duration;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jacobo.adyd.login.exceptions.LoginException;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.val;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class CircuitBreakerCustom {
	
	@Value("${resilience4j.circuitbreaker.instances.dbService.failure-rate-thresholds}")
    private int failureRateThreshold;

    @Value("${resilience4j.circuitbreaker.instances.dbService.wait-duration-in-open-states}")
    private long waitDurationInOpenState;

    @Value("${resilience4j.circuitbreaker.instances.dbService.sliding-window-sizes}")
    private int slidingWindowSize;

    @Value("${resilience4j.circuitbreaker.instances.dbService.minimum-number-of-callss}")
    private int minimumNumberOfCalls;

    @Bean
    public CircuitBreaker dbServiceCircuitBreaker() {
        val config = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenState))
                .slidingWindowSize(slidingWindowSize)
                .minimumNumberOfCalls(minimumNumberOfCalls)
                .ignoreException(customIgnorePredicate())
                .build();

        return CircuitBreaker.of("dbService", config);
    }

    private Predicate<Throwable> customIgnorePredicate() {
    	log.info("lanza el ignore");
        return throwable -> throwable instanceof LoginException &&
                throwable.getMessage().equals("usuario ya creado");
    }

}
