package br.gov.mt.seplag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableCaching
@EnableFeignClients
@EnableScheduling
@EnableWebSocketMessageBroker
@SpringBootApplication
public class CatalogoMusicalApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CatalogoMusicalApiApplication.class, args);
    }

}