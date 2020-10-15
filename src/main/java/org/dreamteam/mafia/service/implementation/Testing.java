package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.User;
import org.dreamteam.mafia.repository.api.CrudUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Testing {


    public static void main(String[] args) {
        SpringApplication.run(Testing.class);
    }

    @Bean
    public CommandLineRunner demo(CrudUserRepository repository) {
        return (args) -> {
            User user3 = repository.findByLogin("user3").get(0);
            System.out.println(user3);
            // save a few customers
          //  repository.save(new User(123, "Dessler"));

        };
    }

}
