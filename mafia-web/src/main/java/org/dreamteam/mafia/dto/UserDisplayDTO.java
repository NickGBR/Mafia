package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Описание комнаты, отправляемое в клиент для ее отображения
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDisplayDTO {

    private String name;
    private Boolean ready;
    private Boolean admin;

    @Override
    public String toString() {
        return "UserDisplayDTO{" +
                "name='" + name + '\'' +
                ", ready=" + ready +
                ", admin=" + admin +
                '}';
    }
}

