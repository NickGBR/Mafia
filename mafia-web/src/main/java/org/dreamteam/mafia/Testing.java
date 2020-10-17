package org.dreamteam.mafia;

import org.dreamteam.mafia.repository.api.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// @ComponentScan("org.dreamteam.mafia")
public class Testing {

    @Bean
    public CommandLineRunner demo(RoomRepository repository) {
        return (args) -> {

         /*   Iterable<RoomDAO> all = repository.findAll();
            for (RoomDAO m : all) {
                System.out.println(m);
            }*/



/*           UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));
        };*/
        };
    }
}
