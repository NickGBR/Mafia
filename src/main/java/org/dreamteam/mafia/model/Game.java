package org.dreamteam.mafia.model;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */
public class Game {
    private boolean isNight = false;
    private String room;
    private boolean isActive = false;

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
