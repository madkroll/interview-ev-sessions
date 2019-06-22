package com.madkroll.inteview.evsessions.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfiguration {

    @Bean
    public ConcurrentHashMap<String, ChargingSession> sessionStorage() {
        return new ConcurrentHashMap<>();
    }
}