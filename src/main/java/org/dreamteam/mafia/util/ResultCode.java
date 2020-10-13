package org.dreamteam.mafia.util;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0),
    PASSWORD_MISMATCH(1),
    USER_ALREADY_EXISTS(2);

    final int value;

    ResultCode(int value) {
        this.value = value;
    }
}
