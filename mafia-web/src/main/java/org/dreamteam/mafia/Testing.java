package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.StatisticsDAO;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.StatisticsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Testing {

    @Bean
    public CommandLineRunner demo(StatisticsRepository repository) {
        return (args) -> {

            Iterable<StatisticsDAO> all = repository.findAll();
            for (StatisticsDAO m : all) {
                System.out.println(m);
            }



/*           UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));
        };*/
        };
    }
}
