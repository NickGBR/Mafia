package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Игра - набор данных, описывающих конкретную игру в целом
 */

@Setter
@Getter
public class Game {

    private boolean isNight = false;
    private String room;
    private Message message;
    private boolean isInterrupted;  // Используется для прерывания игры, если пришел объект Game c isInterrupted = true, то игра останавливается.

//    public boolean isNight() {
//        return isNight;
//    }
//
//    public void setNight(boolean night) {
//        isNight = night;
//    }
//
//    public String getRoom() {
//        return room;
//    }
//
//    public void setRoom(String room) {
//        this.room = room;
//    }
//
//    public boolean isInterrupted() {
//        return isInterrupted;
//    }
//
//    public void setInterrupted(boolean interrupted) {
//        isInterrupted = interrupted;
//    }
//
//    public void setMessage(Message message) {
//        this.message = message;
//    }
//
//    public Message getMessage() {
//        return message;
//    }
}
