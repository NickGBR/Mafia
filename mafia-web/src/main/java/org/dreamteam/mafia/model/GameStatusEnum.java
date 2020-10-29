package org.dreamteam.mafia.model;

/**
 * Состояния комнаты: только созданная, с текущей игрой, с заверешенной игрой, удаленная
 */
public enum GameStatusEnum {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    DELETED
}
