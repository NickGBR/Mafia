package org.dreamteam.mafia.constants;

public class SockConst {
    public static final String SYSTEM_END_POINT = "/system_message";
    public static final String MAFIA_END_POINT = "/mafia_message";
    public static final String CIV_END_POINT = "/civ_message";
    public static final String ROOM_END_POINT = "/room_message";

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

    public static final String REQUEST_GET_ADD_USER_TO_ROOM = "api/GET/addUserToRoom";
    public static final String REQUEST_VOTE_FOR_USER = "/voteForUser";

    public static final String REQUEST_GET_ROOM_ADMIN_NAME = "api/GET/get_room_admin_name";
    public static final String REQUEST_GET_MESSAGES = "api/GET/getMessages";
    public static final String REQUEST_GET_ROOMS = "api/GET/getRooms";
    public static final String REQUEST_GET_USERS = "api/GET/getRoomUsers";
    public static final String REQUEST_POST_CHECK_USER = "api/POST/checkUser";
    public static final String REQUEST_POST_CHECK_ROOM = "api/POST/checkRoom";

    public static final String REQUEST_POST_MESSAGE = "/send";


    public static final String REQUEST_GET_CHANGE_READY_STATUS = "api/POST/changeReadyStatus";
    public static final String REQUEST_GET_ROOM_READY_STATUS = "api/GET/getRoomReadyStatus";

    /**
     * Использоуется для проверки роли персонажа,
     * шериф првоеряет, явлеяется ли персонаж доном,
     * дон проверяет явлеся ли указанный персонаж шерифом.
     */
    public static final String REQUEST_GET_ROLE_INFO = "/GET/checkRole";

    public static final String REQUEST_GET_START_GAME_INFO = "/startGame";





}