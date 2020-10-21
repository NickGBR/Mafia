let userName;
let roomName;
const roomChatId = "room_chat_box";


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
    document.getElementById("send_message_button").disabled = false;
    // Отправляем на сервер информацию, о пользователе вошедшем в чат.
    // Добавляем информацию о пользователе в чат.
    setUserInfoToInterface();
    getUsersMessages();
    stompClient.subscribe(sockConst.ROOM_WEB_CHAT + roomName, getMessage);
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    document.getElementById("room_input").disabled = true;
    document.getElementById("set_room_button").disabled = true;
}

function setUserInfoToInterface() {
    userName = sessionStorage.getItem("login");
    roomName = sessionStorage.getItem("roomName")

    const userInfo = document.createTextNode("User: " + userName + ", room: " + roomName);
    document.getElementById("user_info_label").appendChild(userInfo);
}

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


