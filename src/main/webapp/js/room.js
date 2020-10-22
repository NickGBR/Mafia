let roomName;
let userName;

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
    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_CHAT, addRoomToInterface)
    checkUser();
}

function checkUser() {
    const request = new XMLHttpRequest();
    request.open("POST", "/POST/checkUser", true)
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = request.responseText
                if (data === true) {
                    /* Если комната новая, мы добавляем системное сообщение о новой комнате
                    и выводим ее в список комнат.
                     */
                }
                if (data === "false") {
                    console.log("Авторизированный пользователь, обновил страницу.")
                    userName = null;
                }
            } else if (request.status === 400) {
                const data = JSON.parse(request.responseText);
                switch (parseInt(data["result"])) {
                    case 1: {
                        console.log("Error: Internal logic error");
                        break;
                    }
                    case 2: {
                        console.log("Error: Database error");
                        break;
                    }
                }
            } else if (request.status === 500) {
                console.log("ERROR 500")
            } else {

            }
        }


    }
    const systemMessage = JSON.stringify({
        user: {
            login: sessionStorage.getItem("login"),
            name: sessionStorage.getItem("login")
        }
    })
    request.send(systemMessage);

}

function checkRoom() {
    const request = new XMLHttpRequest();
    request.open("POST", "/POST/checkRoom", true)
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = request.responseText
                if (data === "true") {
                    /* Если комната новая, мы добавляем системное сообщение о новой комнате
                    и выводим ее в список комнат.
                     */
                    sessionStorage.setItem('roomName',roomName);
                    sendMessageToServerAboutNewRoom(roomName);
                    window.location.replace("/chat.html")
                }
                if (data === "false") {
                    alert("Комната " + roomName + " уже существует!");
                    roomName = null;
                }
            } else if (request.status === 400) {

            } else if (request.status === 500) {
                console.log("ERROR 500")
            } else {

            }
        }
    }

    roomName = document.getElementById("room_input").value;
    const str = JSON.stringify({
        'name': roomName,
        'id': roomName
    });
    console.log(str);
    request.send(str);
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    document.getElementById("room_input").disabled = true;
    document.getElementById("set_room_button").disabled = true;
}

/**
 * Проверяет находится ли пользователь в БД или нет, отправляет системное сообщение с параметром
 * checkUser = true;
 */


function getSystemMessage(response) {
}

/**
 * Метод добавляеющий комнату в список комнат.
 * @param response
 */
function addRoomToInterface(response) {
    const data = JSON.parse(response.body);
    roomName = data.room.name;

    //Отправляем сообщение о времени суток
    const dd = document.createElement("dd")           // Создаем элемент списка <dd>
    const button = document.createElement("button")     // Создаем кнопку
    button.setAttribute("id", roomName);                 // Устанавливаем id как название комнаты
    button.setAttribute("onclick", "alert(this.id)"); // Действие при нажатии кнопки
    const buttonName = document.createTextNode(roomName);                 // Создаем текстовый элемент

    button.appendChild(buttonName);
    dd.appendChild(button)// Вставляем текстовый внутрь элемента списка
    document.getElementById('rooms_list').appendChild(dd);
}

/**
 * Метод отправляет всем пользователям информацию, о добавлении новой комнаты.
 * @param roomName
 */
function sendMessageToServerAboutNewRoom(roomName) {

    const data = JSON.stringify({
        'newRoom': true,
        'room': {
            'name': roomName,
            'id': roomName
        }
    })
    stompClient.send(sockConst.SYSTEM_END_POINT, {},data);
}