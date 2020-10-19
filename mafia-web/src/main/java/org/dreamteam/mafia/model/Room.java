package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */

@Setter
@Getter
public class Room {
    private boolean isStarted = false;
    private boolean isExisting = false;
    /* Используется для прерывания игры, если пришел объект Room c isInterrupted = true,
     то игра останавливается.*/
    private boolean isInterrupted = false;
    private boolean isNight = false;
    private String name;
    private String id;
    private Message message;
}
