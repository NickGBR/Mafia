package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class InitDTO implements Serializable {

    Boolean isLoggedIn = false;
    Boolean isInRoom = false;
    String name = "";
    String roomID = "";
    String roomName = "";
    Boolean isAdmin = false;
    Boolean isReady = false;
}

