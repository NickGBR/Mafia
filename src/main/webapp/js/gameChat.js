const gameChatID = "game_chat_box";
let userName;
let userRole;
let roomName;
let roomID;
let isAdmin;
let isReady;
let destination;

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

    // Подписываемся на топики, в зависимости от роли.
    subscribeByRole();

    // Получаем информацию о текущем пользователе.
    showCurrentUserInfo();

    // Получаем историю чата.
    getUsersMessages();
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
    if (userRole === roleConst.MAFIA || userRole === roleConst.DON) {
        console.log("Подписался как мафия");
        stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, getMessage);
        stompClient.subscribe(sockConst.MAFIA_WEB_CHAT + roomID, getMessage)
        stompClient.subscribe(sockConst.SYS_WEB_CHAT+roomID, getGameStat);
    } else {
        console.log("Подписался как мирный на " + sockConst.CIV_WEB_CHAT );
        stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, getMessage);
        stompClient.subscribe(sockConst.SYS_WEB_CHAT+roomID, getGameStat);

    }
}

function setRoleInterface(role) {

    const sendMessageButton = document.getElementById("send_message_button");
    const sendMessageInput = document.getElementById("message_input_value");
    disableInterface();
    if(role===roleConst.CITIZEN){
        sendMessageButton.disabled = false;
        sendMessageInput.disabled = false;
    }
    else if(role === roleConst.MAFIA){
    }
    else  if(role === roleConst.DON){

    }
    else if(role === roleConst.SHERIFF){

    }
}

function disableInterface(){
    document.getElementById('send_message_button').disabled = true;
    document.getElementById('message_input_value').disabled = true;
}





/**
 * Выводит информацю о пользователе в браузер.
 */
function showCurrentUserInfo() {
    const userInfo = document.createTextNode("User: " + userName + " is " + userRole + "!" +
        " Room: " + roomName + ".");
    document.getElementById("user_info_label").appendChild(userInfo);
}

/**
 * Вызывается при получении сообщения на сокет.
 * @param response.
 */
function getMessage(response){
    const data = JSON.parse(response.body);
    // Отправляем сообщение в чат\
    addToChat(data.from + ": " + data.text, gameChatID);
}

/**
 * Добавляет сообщение в HTML.
 * @param text - Текст сообщения.
 * @param chat - id тега для вывода сообщений.
 */
function addToChat(text, chat) {
    const node = document.createElement("LI");      // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);         // Создаем текстовый элемент
    node.appendChild(textNode);                             // Вставляем текстовый внутрь элемента списка
    document.getElementById(chat).appendChild(node);
}

/**
 * Отправляет сообщения пользователей в end point.
 */
function sendMessage() {
    let callback = function (request) {
    };
    const data = JSON.stringify({
        'text': document.getElementById("message_input_value").value,
        'destination': destination,
    })
    sendRequest("POST", sockConst.REQUEST_POST_CIVILIAN_MESSAGE, data, callback, [8]);
}

/**
 * Отправляем серверу название комнаты, для получения истории чата.
 */
function getUsersMessages() {

    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        data.forEach((message) => {
            if(message['destination']===destination.CIVILIAN) {
                addToChat(message["from"] + ": " + message["text"], gameChatID);
            }
        });
    };

    sendRequest("GET", "/api/message/restore", "", callback, [8]);
}

function getGameStat(response){

    const data = JSON.parse(response.body);
    setRoleInterface(data['gamePhase']);
}
function getLog() {
    console.log(userRole);
}

