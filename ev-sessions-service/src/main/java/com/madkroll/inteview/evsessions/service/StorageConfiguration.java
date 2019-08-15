package com.madkroll.inteview.evsessions.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring configuration grouping all storage-specific Spring bean definitions.
 * */
@Configuration
public class StorageConfiguration {

    @Bean
    public ConcurrentHashMap<String, ChargingSession> sessionsPerKey() {
        return new ConcurrentHashMap<>();
    }
}