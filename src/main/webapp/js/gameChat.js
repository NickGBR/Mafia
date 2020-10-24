let userName;
let roomName;
let roomID;
let isAdmin;
let isReady;
const roomChatId = "room_chat_box";
const usersListId = "users_list";
const startButtonHolderId = "admin_button_holder";

function connect() {
    // Подключается через SockJS. Он сам решит использовать ли WebSocket
    // или имитировать их другими средствами
    const socket = new SockJS("http://localhost:8080/chat-messaging");
    console.log("Connected successfully");
    stompClient = Stomp.over(socket);
    // Получаем токен достпа для конкретного пользователя.
    let token = sessionStorage.getItem('token');
    // Пытаемся подключиться, передав токен в заголовке.
    // И две функции: одну для обработки успешного подключения,
    // и вторую для обработки ошибки подключения
    stompClient.connect({'x-auth-token': token}, afterConnect, onError);
}

// Будем вызвано после установления соединения
function afterConnect(connection) {
    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    // Включаем кнопку создания комнаты.
    document.getElementById("set_room_button").disabled = false;
    // Отправляем на сервер информацию, о пользователе вошедшем в чат.
    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_INFO_ADD, updateRoomToInterfaceAdd)
    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_INFO_REMOVE, updateRoomToInterfaceRemove)
    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_INFO_UPDATE, updateRoomToInterface)
    userName = initialisedUserName;
    getRooms();
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    alert("Клиент потерял соединение с сервером");
    document.getElementById("left_room_button").disabled = true;
    document.getElementById("user_ready_button").disabled = true;
}