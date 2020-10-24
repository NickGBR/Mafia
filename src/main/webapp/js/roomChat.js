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
    // Получаем токен доступа для конкретного пользователя.
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

    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    // Включаем кнопку отправки сообщения
    document.getElementById("send_message_button").disabled = false;
    // Отправляем на сервер информацию, о пользователе вошедшем в чат.

    // Добавляем информацию о пользователе в чат.
    showCurrentUserInfo();

    // Получаем историю чата.
    getUsersMessages();

    //Получаем список пользователей
    getRoomUsers();

    // Добавление кнопок для администратора.
    showAdminButton(isAdmin, false);

    changeReadyButtonStatus(isReady)


    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_INFO_REMOVE + roomID, onDisbandment);
    stompClient.subscribe(sockConst.ROOM_WEB_CHAT + roomID, getMessage);
    stompClient.subscribe(sockConst.SYS_WEB_USERS_INFO + roomID, getRoomUsers)
    stompClient.subscribe(sockConst.SYS_USERS_READY_TO_PLAY_INFO + roomID, usersReadyToPlayInfo);
    stompClient.subscribe(sockConst.SYS_GAME_STARTED_INFO + roomID, goToGameChat);
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    alert("Клиент потерял соединение с сервером");
    document.getElementById("left_room_button").disabled = true;
    document.getElementById("user_ready_button").disabled = true;
}

/**
 * Слушает сообщения об удаленных комнатах. Если удалилась эта, то надо перенаправить пользователя обратно на список
 * @param response
 */
function onDisbandment(response) {
    if (!isAdmin) {
        window.location.href = "roomList.html";
        alert("Администратор расформировал комнату. Вы будете возвращены в общее лобби.");
    }
}


/**
 * Выводит информацю о пользователе в браузер.
 */
function showCurrentUserInfo() {

    const userInfo = document.createTextNode("User: " + userName + ", room: " + roomName);
    document.getElementById("user_info_label").appendChild(userInfo);
}

/**
 * Отправляет сообщения пользователей в end point.
 */
function sendMessage() {

    let callback = function (request) {
    };

    sendRequest("POST", "/api/message/send",
        document.getElementById("message_input_value").value, callback, [8]);

}

function getMessage(response) {
    const data = JSON.parse(response.body);

    // Отправляем сообщение в чат
    addToChat(data.from + ": " + data.text, roomChatId);
}

// Добавление сообщения в HTML
function addToChat(text, chat) {
    const node = document.createElement("LI");      // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);         // Создаем текстовый элемент
    node.appendChild(textNode);                             // Вставляем текстовый внутрь элемента списка
    document.getElementById(chat).appendChild(node);
}

/**
 * Отправляем серверу название комнаты, для получения истории чата.
 */
function getUsersMessages() {

    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        data.forEach((message) => {
            addToChat(message["from"] + ": " + message["text"], roomChatId);
        });
    };

    sendRequest("GET", "/api/message/restore", "", callback, [8]);
}

function changeReadyButtonStatus(isReady) {
    const button = document.getElementById("user_ready_button");
    if (!isReady) {
        button.className = "user_ready_button";
        button.value = "Готов"
    } else {
        button.className = "user_not_ready_button";
        button.value = "Не готов";
    }
}


/**
 * Отображает пользователей на экране
 * @param user -  пользователь.
 * @param listId - id эллимента для вывода пользователей.
 */
function showUser(user, listId) {
    const container = document.createElement("span"); // Атрибуд добавления свете нашуму тексту.

    if (user["admin"]) {
        container.className = "admin_user";
    } else if (user["ready"]) {
        container.className = "ready_user";
    } else {
        container.className = "not_ready_user";
    }
    const par = document.createElement("DIV");
    const textNode = document.createTextNode(user["name"]);
    par.appendChild(container);
    container.appendChild(textNode);
    document.getElementById(listId).appendChild(par);
}

/**
 * Добавляет кнопку, начать игру, в указанный эллемент.
 * @param isAdmin - является ли текущий клиент администратором комнаты
 * @param isActive - устанавливает активность кнопки.
 */
function showAdminButton(isAdmin, isActive) {
    if (isAdmin) {
        document.getElementById("admin_button_holder").innerText = "";
        const button = document.createElement('button');
        button.innerText = "Начать игру";
        button.onclick = startGame;
        const holder = document.getElementById(startButtonHolderId);
        if (isActive === true) {
            button.className = "active_start_game_button";
            button.disabled = false;
        } else {
            button.className = "not_active_start_game_button";
            button.disabled = false;
        }
        holder.appendChild(button);

        document.getElementById("left_room_button").setAttribute("value", "Расформировать комнату");
        document.getElementById("left_room_button").setAttribute("onClick", "disbandRoom()");
    }
}

function leaveRoom() {
    let callback = function () {
        window.location.href = "roomList.html";
    };

    sendRequest("POST", "/api/room/leave", "", callback, [8, 11]);
}

function disbandRoom() {
    let callback = function () {
        window.location.href = "roomList.html";
    };
    sendRequest("POST", "/api/room/disband", "", callback, [8, 9]);
}

/**
 * Отвечает за получение пользователей при заходе в комнату.
 */
function getRoomUsers() {

    let callback = function (request) {
        clearUsersList(); // Отчищаем список пользователей, перед обновлением.
        const data = JSON.parse(request.responseText);
        data.forEach((user) => {
            showUser(user, usersListId);
        });
    };

    sendRequest("GET", "/api/room/getUsersList", "", callback, [8]);
}

function clearUsersList() {
    document.getElementById('users_list').innerText = "";
}

/**
 * Меняет состояние готовности пользователя.
 */
function changeUserReadyStatus() {

    let callback = function () {
        isReady = !isReady;
        changeReadyButtonStatus(isReady);
    };
    sendRequest("POST", "/api/room/setReady", !isReady, callback, [8]);
}

function usersReadyToPlayInfo(response) {
    const data = JSON.parse(response.body);
    getRoomUsers();
    showAdminButton(isAdmin, data);
}


function startGame(){
    sendRequest("GET", sockConst.REQUEST_GET_START_GAME_INFO, "", null, [8]);
}


function goToGameChat(response){
    console.log(response);
    const data = JSON.parse(response.body);
    console.log(data);
    window.location.href = "gameChat.html";
}