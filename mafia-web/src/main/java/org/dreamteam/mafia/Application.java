package org.dreamteam.mafia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

    /**
     * Точка входя для приложения Spring Boot
     */
    @SpringBootApplication
    public class Application {

        public static void main(String[] args) {
            ApiContextInitializer.init();
            SpringApplication.run(org.dreamteam.mafia.Application.class, args);
        }
    }

    @RestController
    class HelloController{
        @GetMapping("/")
        public String hello(){
        return "Hello world";
    }
}
