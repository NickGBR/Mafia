package org.dreamteam.mafia;

import org.dreamteam.mafia.dao.CharacterDAO;
import org.dreamteam.mafia.dao.converter.RoleEnum;
import org.dreamteam.mafia.repository.api.CharacterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// @ComponentScan("org.dreamteam.mafia")
public class Testing {

    @Bean
    public CommandLineRunner demo(CharacterRepository repository) {
        return (args) -> {

            List<CharacterDAO> byRole = repository.findByRole(RoleEnum.MAFIA);

            for (CharacterDAO c : byRole) {
                System.out.println(c);
            }


/*            UserDAO user = repository.findByLogin("user1").get(0);
            System.out.println(user);

            repository.save(new UserDAO("123456789", "Poly3"));*/
        };
    }

}
