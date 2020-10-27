var sockConst = {

    SYSTEM_END_POINT: '/app/system_message',
    MAFIA_END_POINT: '/app/mafia_message',
    CIV_END_POINT: '/app/civ_message',
    ROOM_END_POINT: '/app/room_message',

    ROOM_WEB_CHAT: '/chat/room_messages/',

    MAFIA_WEB_CHAT: '/chat/message/send/mafia/',
    CIV_WEB_CHAT: '/chat/message/send/civilians/',
    SYS_WEB_CHAT: '/chat/system_messages/',

    SYS_WEB_ROOMS_INFO_ADD: '/chat/system_messages/rooms/add',
    SYS_WEB_ROOMS_INFO_REMOVE: '/chat/system_messages/rooms/remove',
    SYS_WEB_ROOMS_INFO_UPDATE: '/chat/system_messages/rooms/update',
    SYS_WEB_USERS_INFO: '/chat/system_messages/users/',
    SYS_WEB_USERS_INFO_KICKED: '/chat/system_messages/users/kicked',

    SYS_USERS_READY_TO_PLAY_INFO: '/chat/system_messages/users_ready_to_play_info/',
    SYS_GAME_STARTED_INFO: '/chat/system_message/game_started_message/',

    REQUEST_GET_ADD_USER_TO_ROOM: 'api/GET/addUserToRoom',
    REQUEST_GET_ROOM_ADMIN_NAME: "api/GET/get_room_admin_name",
    REQUEST_GET_MESSAGES: "api/GET/getMessages",
    REQUEST_GET_ROOMS: "api/GET/getRooms",
    REQUEST_GET_USERS: "api/GET/getRoomUsers",
    REQUEST_POST_CHECK_USER: "api/POST/checkUser",
    REQUEST_POST_CHECK_ROOM: "api/POST/checkRoom",

    REQUEST_POST_MESSAGE: "/api/message/send",

    REQUEST_GET_CHANGE_USER_READY_STATUS: "api/POST/changeReadyStatus",
    REQUEST_GET_ROOM_READY_STATUS: "api/GET/getRoomReadyStatus",

    /**
     * Использоуется для проверки роли персонажа,
     * шериф првоеряет, явлеяется ли персонаж доном,
     * дон проверяет явлеся ли указанный персонаж шерифом.
     */
    REQUEST_GET_ROLE_INFO: "api/room/GET/checkRole",


    REQUEST_GET_START_GAME_INFO: '/api/game/startGame'
}

var destinationConst = {

    ROOM_USER: 'ROOM_USER',
    MAFIA: "MAFIA",
    CIVILIAN: "CIVILIAN",
}


