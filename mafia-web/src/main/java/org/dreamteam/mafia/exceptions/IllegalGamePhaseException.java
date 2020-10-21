package org.dreamteam.mafia.exceptions;

public class IllegalGamePhaseException extends Exception {
    public IllegalGamePhaseException() {
    }

    public IllegalGamePhaseException(String message) {
        super(message);
    }
}
