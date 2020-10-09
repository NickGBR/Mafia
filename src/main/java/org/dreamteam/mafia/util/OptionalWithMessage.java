package org.dreamteam.mafia.util;

import java.util.Optional;

/**
 *  Класс-обертка вокруг Optional, позволяющий добавить к нему сообщение с описанием
 *  статуса операции
 */
public class OptionalWithMessage<V> {

    V value;
    String message;

    public OptionalWithMessage(V value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Optional<V> getValue() {
        return Optional.ofNullable(value);
    }
}
