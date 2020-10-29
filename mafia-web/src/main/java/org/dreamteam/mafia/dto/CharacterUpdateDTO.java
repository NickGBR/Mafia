package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterUpdateDTO {

    private String name = "";
    private Boolean isAlive = true;

    public CharacterUpdateDTO(UserDAO dao) {
        this.name = dao.getLogin();
        this.isAlive = dao.getCharacterStatus().equals(CharacterStatusEnum.ALIVE);
    }
}