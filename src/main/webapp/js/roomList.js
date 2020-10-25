//In session storage we have "login", "roomName"

let roomName;
let userName;
let roomEntries = [];
let selectedEntry = null;


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

function joinRoomPublic() {
    room = selectedEntry["room"];
    if (room["privateRoom"]) {
        const modal = document.getElementById("modal-room-join-private");
        const overlay = document.getElementById("overlay-modal");
        const name = document.getElementById("room-name");
        name.innerText = room["name"];
        modal.style.display = "block";
        overlay.style.display = "block";
    } else {
        joinRoom();
    }
}

function joinRoomPrivate() {
    const modal = document.getElementById("modal-room-join-private");
    const overlay = document.getElementById("overlay-modal");
    const password = document.getElementById("password_input_join");
    modal.style.display = "none";
    overlay.style.display = "none";
    password.value = "";
}

function joinRoom() {

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
    const dd = document.getElementById("dd" + room["id"]);
    document.getElementById('rooms_list').removeChild(dd);
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
    let roomEntry = {};
    roomEntry["room"] = room;
    let lastNode = document.getElementById("room_entry_last");
    const copyNode = lastNode.cloneNode(true);
    copyNode.id = "room_entry_" + room["id"];
    const text = copyNode.querySelector('.text');
    const nameTextNode = document.createTextNode(room["name"] + "   | ");
    const currPlayersNode = document.createElement("SPAN");
    currPlayersNode.innerText = room["currPlayers"];
    currPlayersNode.style.fontStyle = "italic";
    const maxPlayersNode = document.createTextNode(" / " + room["maxPlayers"]);
    text.appendChild(nameTextNode);
    text.appendChild(currPlayersNode);
    text.appendChild(maxPlayersNode);
    text.classList.add("clickable");
    text.onclick = function (event) {
        selectRoom(event.target);
        updateButtonsOnSelect();
    }
    if (room["privateRoom"]) {
        const stampContainer = copyNode.querySelector('.stamp-container');
        const stampNode = document.createElement("span");
        stampNode.classList.add("stamp");
        stampNode.classList.add("red");
        stampNode.innerText = "Секретно";
        stampNode.style.transform = 'rotate(' + (Math.random() * (10) - 5) + 'deg) translateY(-0.5rem)';
        stampContainer.appendChild(stampNode);
    }
    const list = document.getElementById("rooms_list");
    list.insertBefore(copyNode, list.firstChild);
    roomEntry["node"] = copyNode;
    roomEntries.push(roomEntry);
}

function selectRoom(node) {
    const foundEntry = roomEntries.find(element => element["node"].querySelector('.text') === node);
    if (selectedEntry == undefined) {
        selectedEntry = null;
    }
    if (selectedEntry !== null) {
        const selectedText = selectedEntry["node"].querySelector('.text');
        selectedText.classList.remove("selected");
        if (selectedText === node) {
            selectedEntry = null;
            return;
        }
        selectedEntry = null;
    }
    selectedEntry = foundEntry;
    const selectedText = selectedEntry["node"].querySelector('.text');
    selectedText.classList.add("selected");
}

function updateButtonsOnSelect() {
    let button = document.getElementById("join_room button");
    button.disabled = selectedEntry == null;
}


/**
 * Отвечает за переход пользователя в комнату чата, если комната не заполнена.
 * @param id - уникальные индификатор комнаты.
 */
function tryToGoToRoom(id) {
    addUserToRoom(id)
}

