package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dto.RoomDisplayDTO;

@Getter
@Setter
@NoArgsConstructor
public class SystemMessage {
    Room room;
    RoomDisplayDTO roomDTO;
    Message message;
    User user;
    boolean isNewUser = false;
    boolean isNewRoom = false;
    boolean addUserToRoom = false;
    boolean remove = false;
}
