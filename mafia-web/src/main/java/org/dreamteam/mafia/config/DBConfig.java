package org.dreamteam.mafia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Класс для настроеек параметров подключений к БД
 */
@Configuration
@EnableJpaRepositories("org.dreamteam.mafia.repository.api")
public class DBConfig {

    /**
     * Bean для получения источника данных, подключенного к БД
     *
     * @return - настроенный источник данных
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("zgvttnjlfzgmjp");
        dataSource.setPassword("876519693b4f46d05ee23620e4181e654907eb71ac21ffb7cb14b4b587693504");
        dataSource.setUrl("jdbc:postgresql://ec2-54-246-115-40.eu-west-1.compute.amazonaws.com:5432/d7583h6on7o1b3");
        return dataSource;
    }
}
