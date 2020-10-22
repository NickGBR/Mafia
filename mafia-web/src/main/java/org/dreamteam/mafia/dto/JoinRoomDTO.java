package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Набор данных для входа в комнату, принимаемое из клиента
 */
@Getter
@Setter
@NoArgsConstructor
public class JoinRoomDTO implements Serializable {

    private Long id;
    private String password;

    @Override
    public String toString() {
        return "JoinRoomDTO{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
