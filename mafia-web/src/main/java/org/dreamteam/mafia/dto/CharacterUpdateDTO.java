package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.entities.UserEntity;
import org.dreamteam.mafia.model.CharacterStatusEnum;

/**
 * Описание обновления состояния персонажа в игре для отправки клиенту
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterUpdateDTO {

    private String name = "";
    private Boolean isAlive = true;

    public CharacterUpdateDTO(UserEntity dao) {
        this.name = dao.getLogin();
        this.isAlive = dao.getCharacterStatus().equals(CharacterStatusEnum.ALIVE);
    }
}