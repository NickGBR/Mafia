package org.dreamteam.mafia.model;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */
public class Game {
    private boolean isNight = false;
    private String room;
    private boolean isInterrupted;  // Используется для прерывания игры, если пришел объект Game c isInterrupted = true, то игра останавливается.

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

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }
}
