package org.dreamteam.mafia.service.implementation.engine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "engine")
@Getter
@Setter
@NoArgsConstructor
public class GamePhaseDurationsService {
    private HashMap<String, Integer> durations;
}
