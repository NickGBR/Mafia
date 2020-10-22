let userName;
let roomName;
let roomAdminName;
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

    roomName = sessionStorage.getItem('roomName');

    userName = sessionStorage.getItem('userName');

    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    // Включаем кнопку создания комнаты.
    document.getElementById("send_message_button").disabled = false;
    // Отправляем на сервер информацию, о пользователе вошедшем в чат.

    // Добавляем информацию о пользователе в чат.
    showCurrentUserInfo();

    // Получаем историю чата.
    getUsersMessages();

    //Получаем список пользователей
    getRoomUsers();

    // Полчучем имя админа комнаты.
    getRoomAdminName();

    // Добавление кнопок для администратора.
    showAdminButton(roomAdminName, false);


    stompClient.subscribe(sockConst.ROOM_WEB_CHAT + roomName, getMessage);
    stompClient.subscribe(sockConst.SYS_WEB_USERS_INFO + roomName, updateUsersInfo)
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    document.getElementById("set_room_button").disabled = true;
}

/**
 * Выводит информацю о пользователе в браузер.
 */
function showCurrentUserInfo() {
    userName = sessionStorage.getItem("login");
    roomName = sessionStorage.getItem("roomName")

    const userInfo = document.createTextNode("User: " + userName + ", room: " + roomName);
    document.getElementById("user_info_label").appendChild(userInfo);
}

/**
 * Отправляет сообщения пользователей в end point.
 */
function sendMessage() {
    const message = JSON.stringify({
        // Это работает на JQuery
        // 'message': $("#message_input_value").val()
        // А это на чистом JavaScript
        'roomName': roomName,                                    // Сервер должен знать в какую комнату переслать сообщение.
        'text': document.getElementById("message_input_value").value,
        'from': userName
    });
    console.log(message);
    stompClient.send(sockConst.ROOM_END_POINT, {}, message);
}

function getMessage(response) {
    const data = JSON.parse(response.body);
    console.log("Получено сообщение: " + data.text)
    console.log("Название чата: " + data.roomName);
    console.log("From = " + data.from)

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
    const request = new XMLHttpRequest();
    request.open("GET", sockConst.REQUEST_GET_MESSAGES + "?roomName=" + roomName, true)
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer" + sessionStorage.getItem('token'));

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = JSON.parse(request.responseText);
                console.log(data);
                data.forEach((message) => {
                    addToChat(message.from + ": " + message.text, roomChatId);
                });

            } else if (request.status === 400) {

            } else if (request.status === 500) {
                const data = JSON.parse(request.responseText);
                if (data.answerMessage === null) {
                    console.log("В истории чата нет сообщений.")
                }

                console.log("ERROR 500")
            } else {

            }
        }
    }
    request.send();
}

var checkButtonStat = false;

function setReadyStatus() {
    const button = document.getElementById("user_ready_button");
    if (checkButtonStat) {
        button.className = "user_ready_button";
        button.value = "Готов"
    } else {
        button.className = "user_not_ready_button";
        button.value = "Не готов";
    }
    checkButtonStat = !checkButtonStat;
}

/**
 * Отвечает за обновление информации о пользователях.
 * Когда в комнату заходит новый пользователь, сервер отправляет
 * сообщение всем пользователям комнаты, со списком пользователей комнаты.
 * @param response
 */
function updateUsersInfo(response) {
    clearUsersList();
    document.getElementById('users_list').innerText = "";
    const data = JSON.parse(response.body);
    console.log(data);
    data.forEach((user) => {
        showUser(user.name, usersListId);
    });
}

/**
 * Отображает пользователей на экране
 * @param name - имя пользователя.
 * @param listId - id эллимента для вывода пользователей.
 */
function showUser(name, listId) {
    const container = document.createElement("span"); // Атрибуд добавления свете нашуму тексту.

    if (roomAdminName === name) {
        container.className = "admin_user";
    } else {
        container.className = "not_ready_user";
    }
    const par = document.createElement("DIV");
    const textNode = document.createTextNode(name);
    par.appendChild(container);
    container.appendChild(textNode);

    document.getElementById(listId).appendChild(par);
}

/**
 * Добавляет кнопку, начать игру, в указанный эллемент.
 * @param adminName - имя админимтратора, кнопка создается только для администратора.
 * @param isActive - устанавливает активность кнопки.
 */
function showAdminButton(adminName, isActive) {
        if(adminName===userName) {
            const button = document.createElement('button');
            button.innerText = "Начать игру";
            const holder = document.getElementById(startButtonHolderId);
            if (isActive === true) {
                button.className = "active_start_game_button";
                button.disabled = false;
            } else {
                button.className = "not_active_start_game_button";
                button.disabled = false;
            }
            holder.appendChild(button);
        }
}

/**
 * Отвечает за получение пользователей при заходе в комнату.
 */
function getRoomUsers() {
    const request = new XMLHttpRequest();
    request.open("GET", sockConst.REQUEST_GET_USERS + "?roomName=" + roomName, true)
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer" + sessionStorage.getItem('token'));

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            clearUsersList(); // Отчищаем список пользователей, перед обновлением.
            if (request.status === 200) {
                const data = JSON.parse(request.responseText);
                data.forEach((user) => {
                    showUser(user.name, usersListId);
                });
            } else if (request.status === 400) {
                console.log("ERROR 400");
            } else if (request.status === 500) {
                console.log("ERROR 500");
            } else {
                console.log("ERROR, JUST ERROR");
            }
        }
    }
    request.send();
}

/**
 * Получает имя администратора, данный запрос синхронный, так как нам важно получить имя
 * администратора до вывода списка пользователей, если сдлеать запрос не синхронным,
 * могут возникать ситуации, при которых администратор не будет подсвесиваться. Данный запос
 * делает только при первом входе в комнату.
 */
function getRoomAdminName() {
    const request = new XMLHttpRequest();
    request.open("GET", sockConst.REQUEST_GET_ROOM_ADMIN_NAME + "?roomName=" + roomName, false);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer" + sessionStorage.getItem('token'));
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                roomAdminName = request.responseText;
                console.log(roomAdminName);
            } else if (request.status === 400) {
                console.log("ERROR 400");
            } else if (request.status === 500) {
                console.log("ERROR 500 in get room admin");
            } else {
                console.log("ERROR, JUST ERROR");
            }
        }
    }
    request.send();
}

function clearUsersList() {
    document.getElementById('users_list').innerText = "";
}
