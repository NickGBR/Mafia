package org.dreamteam.mafia.model;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */
public class Game {
    boolean isNight = false;

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
