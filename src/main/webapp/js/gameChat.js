let userName;
let userRole;
let roomName;
let roomID;
let isAdmin;
let isReady;

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

    roomName = initialisedRoomName;
    roomID = initialisedRoomID;
    userName = initialisedUserName;
    isAdmin = initialisedIsAdmin;
    isReady = initialisedIsReady;
    userRole = initialisedUserRole;

    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    subscribeByRole();
    showCurrentUserInfo();
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    alert("Клиент потерял соединение с сервером");
    document.getElementById("left_room_button").disabled = true;
    document.getElementById("user_ready_button").disabled = true;
}

/**
 * Подписывает пользователей на топики, в зависимости от роли в игре.
 */
function subscribeByRole() {
    if (userRole === roleConst.MAFIA || userRole === roleConst.DONE) {
        stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, getMessage);
        stompClient.subscribe(sockConst.MAFIA_WEB_CHAT + roomID, getMessage)
    } else {
        stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, getMessage);
    }
}

function setRoleInterface() {
}

/**
 * Выводит информацю о пользователе в браузер.
 */
function showCurrentUserInfo() {
    const userInfo = document.createTextNode("User: " + userName + " is " + userRole + "!" +
        " Room: " + roomName + ".");
    document.getElementById("user_info_label").appendChild(userInfo);
}

function getMessage(){
}

function disableInterface(){
    document.getElementById('send_message_button').disabled = true;
    document.getElementById('message_input_value').disabled = true;
}

function getLog() {
    console.log(userRole);
}

