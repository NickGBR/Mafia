package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dao.StatisticsDAO;
import org.dreamteam.mafia.dao.VotingDAO;
import org.dreamteam.mafia.model.Room;
import org.dreamteam.mafia.repository.api.RoomRepository;
import org.dreamteam.mafia.repository.api.StatisticsRepository;
import org.dreamteam.mafia.repository.api.VotingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Testing {

    @Bean
    public CommandLineRunner demo(VotingRepository repository) {
        return (args) -> {

            Iterable<VotingDAO> all = repository.findAll();
            for (VotingDAO r : all) {
                System.out.println(r);
            }




/*           UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));
        };*/
        };
    }
}
