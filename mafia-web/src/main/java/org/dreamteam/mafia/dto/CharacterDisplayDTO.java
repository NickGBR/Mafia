package org.dreamteam.mafia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.CharacterEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDisplayDTO {

    private String name;
    private Boolean isAlive;
    private CharacterEnum role;
}
