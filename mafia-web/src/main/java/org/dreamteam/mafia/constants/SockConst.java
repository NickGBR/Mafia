package org.dreamteam.mafia.constants;

/**
 *
 */
public class SockConst {
    public static final String ROOM_WEB_CHAT = "/chat/room_messages/";
    public static final String MAFIA_WEB_CHAT = "/chat/message/send/mafia/";
    public static final String CIV_WEB_CHAT = "/chat/message/send/civilians/";
    public static final String SYS_WEB_CHAT = "/chat/system_messages/";

    public static final String SYS_WEB_ROOMS_INFO_ADD = "/chat/system_messages/rooms/add";
    public static final String SYS_WEB_ROOMS_INFO_REMOVE = "/chat/system_messages/rooms/remove";
    public static final String SYS_WEB_ROOMS_INFO_UPDATE = "/chat/system_messages/rooms/update";
    public static final String SYS_WEB_USERS_INFO = "/chat/system_messages/users/";
    public static final String SYS_WEB_USERS_INFO_KICKED = "/chat/system_messages/users/kicked";
    public static final String SYS_USERS_READY_TO_PLAY_INFO = "/chat/system_messages/users_ready_to_play_info/";
    public static final String SYS_GAME_STARTED_INFO = "/chat/system_message/game_started_message/";
    public static final String SYS_WEB_CHARACTER_INFO_UPDATE = "/chat/system_message/game/character_update/";

    public static final String REQUEST_VOTE_FOR_USER = "/voteForUser";
    public static final String REQUEST_SEND_MESSAGE = "/send";
    public static final String REQUEST_LOAD_MESSAGES = "/restore";
    /**
     * Использоуется для проверки роли персонажа,
     * шериф првоеряет, явлеяется ли персонаж доном,
     * дон проверяет явлеся ли указанный персонаж шерифом.
     */
    public static final String REQUEST_GET_OTHER_ROLE_INFO = "/checkRole";
    public static final String REQUEST_GET_START_GAME_INFO = "/startGame";
}