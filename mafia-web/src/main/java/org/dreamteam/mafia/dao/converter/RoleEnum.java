package org.dreamteam.mafia.dao.converter;

import lombok.Getter;

@Getter
public enum RoleEnum {
    DON("DON"),
    MAFIA("MAFIA"),
    SHERIFF("SHERIFF"),
    CITIZEN("CITIZEN");

    private final String code;

    RoleEnum(String code) {
        this.code = code;
    }
}
