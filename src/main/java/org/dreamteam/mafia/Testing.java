package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.repository.api.CrudUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// @ComponentScan("org.dreamteam.mafia")
public class Testing {

    @Bean
    public CommandLineRunner demo(CrudUserRepository repository) {
        return (args) -> {
            UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));
        };
    }

}
