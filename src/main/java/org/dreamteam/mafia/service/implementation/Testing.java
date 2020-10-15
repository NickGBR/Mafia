package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.repository.api.CrudUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

// @SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan("org.dreamteam.mafia")
public class Testing {


    public static void main(String[] args) {
        SpringApplication.run(Testing.class);
    }

    @Bean
    public CommandLineRunner demo(CrudUserRepository repository) {
        return (args) -> {
            UserDAO user3 = repository.findByLogin("Poly").get(0);
            System.out.println(user3);
            Iterable<UserDAO> usersInDb = repository.findAll();

            repository.save(new UserDAO("123456789", "Poly2"));
            // save a few customers
          //  repository.save(new User(123, "Dessler"));

        };


    }

}
