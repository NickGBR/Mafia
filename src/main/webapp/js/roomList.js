//In session storage we have "login", "roomName"

let roomName;
let userName;
let isLastRoomUser = false;

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

function getRooms() {

    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        console.log(data);
        data.forEach((room) => {
            addRoomToInterface(room);
        });
    };
    sendRequest("GET", "/api/room/getInitialList", "", callback, []);
}

/**
 * Добавляет пользователя в комнату, если комната заполненна,
 * то выдает ошибку.
 * @param roomId
 */
function addUserToRoom(roomId) {

    let callback = function () {
        window.location.href = "roomChat.html";
    };
    const jsonData = {
        'id': roomId,
        'password': ''
    };
    sendRequest("POST", "/api/room/join", JSON.stringify(jsonData), callback, [4, 5, 10, 11, 12]);
}


/**
 * Метод проверки наличия комнаты в БД, позволяем избежать добавления двух одинаковых комнат.
 */
function checkRoom() {

    let callback = function () {
        window.location.href = "roomChat.html";
    };

    roomName = document.getElementById("room_input").value;
    const jsonData = {
        'name': roomName,
        'description': 'New room',
        'maxPlayers': gameConst.USERS_AMOUNT,
    };

    sendRequest("POST", "/api/room/create", JSON.stringify(jsonData), callback, [5]);
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    alert("Клиент потерял соединение с сервером");
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
function updateRoomToInterfaceAdd(response) {
    const room = JSON.parse(response.body);
    addRoomToInterface(room);
}

/**
 * Метод удаляющий комнату из списка комнат.
 * @param response
 */
function updateRoomToInterfaceRemove(response) {
    const room = JSON.parse(response.body);
    if (data["remove"]) {
        removeRoomFromInterface(room);
    } else {
        addRoomToInterface(room);
    }
}

/**
 * Метод добавляеющий новую комнату в список комнат.
 * @param response
 */
function updateRoomToInterface(response) {
    const room = JSON.parse(response.body);
    const button = document.getElementById(room["id"]);
    button.textContent = '';
    const label = room["name"] + "   | " + room["currPlayers"] + " / " + room["maxPlayers"];
    const buttonName = document.createTextNode(label);                 // Создаем текстовый элемент

    button.appendChild(buttonName);
}

/**
 * Метод добавляеющий комнату в список комнат по имени, используется для добавление старых комнат в
 * интерфейс пользователя.
 * @param room
 */
function addRoomToInterface(room) {
    const dd = document.createElement("dd")           // Создаем элемент списка <dd>
    dd.setAttribute("id", "dd" + room["id"])
    const button = document.createElement("button")     // Создаем кнопку
    button.setAttribute("id", room["id"]);                 // Устанавливаем id
    button.setAttribute("description", room["description"])
    button.setAttribute("onclick", "tryToGoToRoom(this.id)"); // Действие при нажатии на комнату, попытаемся перейти в нее.
    const label = room["name"] + "   | " + room["currPlayers"] + " / " + room["maxPlayers"];
    const buttonName = document.createTextNode(label);                 // Создаем текстовый элемент

    button.appendChild(buttonName);
    dd.appendChild(button)// Вставляем кнопку внутрь элемента списка
    document.getElementById('rooms_list').appendChild(dd);
}

/**
 * Отвечает за переход пользователя в комнату чата, если комната не заполнена.
 * @param id - уникальные индификатор комнаты.
 */
function tryToGoToRoom(id) {
    addUserToRoom(id)
}

