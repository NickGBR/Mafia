package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemMessage {
    Room room;
    Message message;
    User user;
    boolean isNewUser = false;
    boolean isNewRoom = false;
}
