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
    stompClient.subscribe(sockConst.ROOM_WEB_CHAT, receiveMessage);
    userName = initialisedUserName;
    getRooms();
    loadChatMessages();
    stopSpinner();
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




function openRoomCreator() {
    document.getElementById("new_room_name").value = "";
    document.getElementById("new_room_max_amount").value = "10";
    document.getElementById("new_room_mafia_amount").value = "3";
    document.getElementById("new_room_don").checked = true;
    document.getElementById("new_room_sheriff").checked = true;
    document.getElementById("input-error-message").style.display = "none";
    setupModal("modal-room-create");
}

function joinRoomPublic() {
    room = selectedEntry["room"];
    if (room["privateRoom"]) {
        setupModal("modal-room-join-private");
        const name = document.getElementById("room-name");
        name.innerText = room["name"];
    } else {
        const jsonData = {
            'id': room["id"],
            'password': ''
        };
        joinRoom(jsonData);
    }
}

function joinRoomPrivate() {
    hideModal();
    room = selectedEntry["room"];
    const password = document.getElementById("password_input_join");
    const jsonData = {
        'id': room["id"],
        'password': password.value
    };
    password.value = "";
    joinRoom(jsonData);
}

function joinRoom(jsonData) {
    let callback = function () {
        window.location.href = "roomChat.html";
    };
    sendRequest("POST", "/api/room/join", JSON.stringify(jsonData), callback, [4, 5, 10, 11, 12]);
}


/**
 * Пытаемся создать новую комнату
 */
function createRoom() {

    let name = document.getElementById("new_room_name").value;
    name = name.trim();
    if (name.length === 0) {
        showInputError("Ошибка: пустое имя комнаты");
        return;
    }
    if (name.length > 20) {
        showInputError("Ошибка: слишком длинное имя комнаты");
        return;
    }
    const maxAmount = document.getElementById("new_room_max_amount").value;
    if (maxAmount < 3 || maxAmount > 99) {
        showInputError("Ошибка: недопустимое число игроков");
        return;
    }
    const mafiaAmount = document.getElementById("new_room_mafia_amount").value;
    if (mafiaAmount < 1 || mafiaAmount > 33) {
        showInputError("Ошибка: недопустимое число игроков за Мафию");
        return;
    }
    if (mafiaAmount > (maxAmount / 2 + maxAmount % 2 - 1)) {
        showInputError("Ошибка: слишком много игроков за Мафию от общего числа");
        return;
    }

    hideModal();

    let callback = function () {
        window.location.href = "roomChat.html";
    };

    const jsonData = {
        'name': name,
        'description': '',
        'password': document.getElementById("new_room_password").value,
        'maxPlayers': maxAmount,
        'mafia': mafiaAmount,
        'don': document.getElementById("new_room_don").checked,
        'sheriff': document.getElementById("new_room_sheriff").checked
    };

    sendRequest("POST", "/api/room/create", JSON.stringify(jsonData), callback, [5, 13, 14]);
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    showModalMessage("Ошибка", "Клиент потерял соединение с сервером");
    document.getElementById("room_input").disabled = true;
    document.getElementById("set_room_button").disabled = true;
}


function showInputError(text) {
    const error = document.getElementById("input-error-message")
    error.innerText = text;
    error.style.display = "block";
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
    const foundEntry = roomEntries.find(element => element["room"]["id"] === room["id"]);
    document.getElementById('rooms_list').removeChild(foundEntry["node"]);
}

/**
 * Метод добавляеющий новую комнату в список комнат.
 * @param response
 */
function updateRoomToInterface(response) {
    const room = JSON.parse(response.body);
    const foundEntry = roomEntries.find(element => element["room"]["id"] === room["id"]);
    foundEntry["room"] = room;
    const text = foundEntry["node"].querySelector('.text');
    text.textContent = "";
    addRoomText(text, room);

}

function addRoomText(text, room) {
    const nameTextNode = document.createTextNode(room["name"] + "   | ");
    const currPlayersNode = document.createElement("SPAN");
    currPlayersNode.innerText = room["currPlayers"];
    currPlayersNode.style.fontStyle = "italic";
    const maxPlayersNode = document.createTextNode(" / " + room["maxUserAmount"]);
    text.appendChild(nameTextNode);
    text.appendChild(currPlayersNode);
    text.appendChild(maxPlayersNode);
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
    addRoomText(text, room)
    text.classList.add("clickable");
    text.onclick = function (event) {
        selectRoom(event.target);
        updateButtonsOnSelect();
    }

    addDescriptionTooltip(copyNode, room);

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
    if (selectedEntry === undefined) {
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
    let button = document.getElementById("join_room_button");
    button.disabled = selectedEntry == null;
}

function sendMessageToCommon() {
    sendMessage();
}