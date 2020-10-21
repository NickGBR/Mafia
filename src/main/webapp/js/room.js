//In session storage we have "login" and "roomName"

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
    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_CHAT, addNewRoomToInterface)
    checkUser();
    getRooms();
}

function getRooms() {
    const request = new XMLHttpRequest();
    request.open("GET", sockConst.REQUEST_GET_ROOMS, true)
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = JSON.parse(request.responseText);
                console.log(data);
                data.forEach((room) => {
                addRoomToInterface(room.name);
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
 * Добавление пользователя во временную БД.
 */
function checkUser() {
    userName = sessionStorage.getItem('login');
    const request = new XMLHttpRequest();
    request.open("POST", sockConst.REQUEST_POST_CHECK_USER, true);
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = request.responseText;
                if (data === true) {
                    /*
                    Если новый пользователь, добавлен во временную БД, мы попадем в это место.
                     */
                }
                if (data === "false") {
                    console.log("Авторизированный пользователь, обновил страницу.")
                }
            } else if (request.status === 400) {

            } else if (request.status === 500) {
                console.log("ERROR 500");
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

/**
 * Метод проверки наличия комнаты в БД, позволяем избежать добавления двух одинаковых комнат.
 */
function checkRoom() {
    const request = new XMLHttpRequest();
    request.open("POST", sockConst.REQUEST_POST_CHECK_ROOM, true)
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = request.responseText
                if (data === "true") {
                    /* Если комната новая, мы добавляем системное сообщение о новой комнате
                    и выводим ее в список комнат.
                     */
                    sessionStorage.setItem('roomName', roomName);
                    sendMessageToServerAboutNewRoom(roomName);
                    window.open("roomChat.html", "_self");
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
 * Метод добавляеющий новую комнату в список комнат.
 * @param response
 */
function addNewRoomToInterface(response) {
    const data = JSON.parse(response.body);
    roomName = data.room.name;

addRoomToInterface(roomName);
}

/**
 * Метод добавляеющий комнату в список комнат по имени, используется для добавление старых комнат в
 * интерфейс пользователя.
 * @param name
 */
function addRoomToInterface(name) {
    const dd = document.createElement("dd")           // Создаем элемент списка <dd>
    const button = document.createElement("button")     // Создаем кнопку
    button.setAttribute("id", name);                 // Устанавливаем id как название комнаты
    button.setAttribute("onclick", "goToRoom(this.id)"); // Действие при нажатии на комнату, переходим в нее.
    const buttonName = document.createTextNode(name);                 // Создаем текстовый элемент

    button.appendChild(buttonName);
    dd.appendChild(button)// Вставляем текстовый внутрь элемента списка
    document.getElementById('rooms_list').appendChild(dd);
}

function goToRoom(id){
    window.open("roomChat.html", "_self");
    sessionStorage.setItem("roomName",id);
    sessionStorage.setItem("roomId",roomName);
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
    stompClient.send(sockConst.SYSTEM_END_POINT, {}, data);
}