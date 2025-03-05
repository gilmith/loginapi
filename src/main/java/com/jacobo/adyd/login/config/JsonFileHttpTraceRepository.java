package com.jacobo.adyd.login.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JsonFileHttpTraceRepository implements HttpExchangeRepository {

    private final List<HttpExchange> traces = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper para JSON


	@Override
	public void add(HttpExchange trace) {
		this.traces.add(trace);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = LocalDateTime.now().format(formatter);

            // Convertir la traza a JSON
            String jsonTrace;
            try {
            	objectMapper.registerModule(new JavaTimeModule());
                jsonTrace = objectMapper.writeValueAsString(trace);
                log.info(jsonTrace);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                jsonTrace = "{\"error\": \"Error al convertir la traza a JSON: " + e.getMessage() + "\"}";
                e.printStackTrace(); // Loguear el error de conversión
            }

           
        
		
	}


	@Override
	public List<HttpExchange> findAll() {
        return Collections.unmodifiableList(this.traces);

	}
}
