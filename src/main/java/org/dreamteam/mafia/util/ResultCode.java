package org.dreamteam.mafia.util;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0),
    PASSWORD_MISMATCH(1),
    USER_ALREADY_EXISTS(2),
    USER_NOT_EXISTS(3),
    INCORRECT_PASSWORD(4);

    final int value;

    ResultCode(int value) {
        this.value = value;
    }
}
