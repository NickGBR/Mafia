package org.dreamteam.mafia.model;

/**
 * Адресаты для сообщений: пользователи в комнате, игроки за мафию, игроки в игре, игроки в общем чате списка комнат
 */
public enum DestinationEnum {
    ROOM_USER,
    MAFIA,
    CIVILIAN,
    COMMON
}
