package org.dreamteam.mafia.util;

import lombok.Getter;

/**
 * Перечисление ошибок, возвращаемых с кодами HTTP 400
 */
@Getter
public enum ClientErrorCode {
    INVALID_REQUEST(-1),
    SECURITY_VIOLATION(0),
    PASSWORD_MISMATCH(1),
    USER_ALREADY_EXISTS(2),
    USER_NOT_EXISTS(3),
    INCORRECT_PASSWORD(4),
    ALREADY_IN_ROOM(5),
    WRONG_GAME_PHASE(6),
    ROOMS_MISMATCH(7),
    NOT_IN_ROOM(8),
    NOT_ENOUGH_RIGHTS(9),
    ROOM_NOT_EXIST(10),
    GAME_ALREADY_STARTED(11),
    ROOM_IS_FULL(12),
    INVALID_NAME(13),
    INVALID_ROOM_PARAMETERS(14),
    CHARACTER_IS_DEAD(15);

    private final int value;

    ClientErrorCode(int value) {
        this.value = value;
    }
}
