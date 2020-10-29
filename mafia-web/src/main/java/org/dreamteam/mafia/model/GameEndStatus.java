package org.dreamteam.mafia.model;

/**
 * Состояние окончания игра: окончена по времени, по победе мафии, по победе мирных, не окончена
 */
public enum GameEndStatus {
    LAST_DAY_CAME,
    MAFIA_WON,
    CIVILIANS_WON,
    GAME_NOT_ENDED
}
