package com.wardzionn.githubproxy.configuration;

import com.wardzionn.githubproxy.exception.external.MedicalclinicErrorDecoder;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new MedicalclinicErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100L, 1000L, 3);
    }
}