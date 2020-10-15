package org.dreamteam.mafia.util;

import lombok.Getter;

@Getter
public enum ServerErrorCode {
    SERVER_CODE_FAILURE(1),
    DB_FAILURE(2);

    final int value;

    ServerErrorCode(int value) {
        this.value = value;
    }
}
