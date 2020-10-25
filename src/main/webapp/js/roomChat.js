let userName;
let roomName;
let roomID;
let isAdmin;
let isReady;
let maxUserAmount;
let selectedEntry = null;
const usersListId = "users_list";

let userEntries = [];

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
    // и вторую для обработки ошибки подключени
    stompClient.connect({'x-auth-token': token}, afterConnect, onError);
}

// Будем вызвано после установления соединения
function afterConnect(connection) {

    userName = init["name"];
    roomID = init["roomID"];
    roomName = init["roomName"];
    isAdmin = init["isAdmin"];
    isReady = init["isReady"];
    maxUserAmount = init["maxUserAmount"];

    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    // Включаем кнопку отправки сообщения
    document.getElementById("send_message_button").disabled = false;

    // Выводим назание комнаты и заполняем список игроков
    initCurrentRoomInfo();
    // Выбираем какой набор кнопок отображать: для админа или для пользователя
    // и для пользователя инициализируем кнопку готовности.
    initButtons();

    stompClient.subscribe(sockConst.SYS_WEB_ROOMS_INFO_REMOVE + roomID, onDisbandment);
    stompClient.subscribe(sockConst.ROOM_WEB_CHAT + roomID, receiveMessage);
    stompClient.subscribe(sockConst.SYS_WEB_USERS_INFO + roomID, loadRoomUsers)
    stompClient.subscribe(sockConst.SYS_USERS_READY_TO_PLAY_INFO + roomID, updateUsersReadiness)

    // Получаем историю чата.
    loadChatMessages();
    //Получаем список пользователей
    loadRoomUsers();
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    alert("Клиент потерял соединение с сервером");
    document.getElementById("left_room_button").disabled = true;
    document.getElementById("user_ready_button").disabled = true;
}

/**
 * Выводит информацю о пользователе в браузер.
 */
function initCurrentRoomInfo() {
    const roomNameText = document.createTextNode(roomName);
    document.getElementById("roomName").appendChild(roomNameText);
    let firstNode = document.getElementById("user_entry_1");
    let userEntry = initUserEntry(firstNode);
    userEntries.push(userEntry);
    for (let i = 1; i < maxUserAmount; i++) {
        const copyNode = firstNode.cloneNode(true);
        copyNode.id = "user_entry_" + (i + 1);
        document.getElementById(usersListId).appendChild(copyNode);
        let userEntry = initUserEntry(copyNode);
        userEntries.push(userEntry);
    }
    console.log(userEntries);
}

function initUserEntry(node) {
    let userEntry = {};
    userEntry["login"] = "";
    userEntry["admin"] = false;
    userEntry["used"] = false;
    userEntry["stamp-container"] = node.querySelector('.stamp-container');
    userEntry["text"] = node.querySelector('.text');
    return userEntry;
}


/**
 * Устанавливает соответствующий пользователю набор кнопок для отображения
 */
function initButtons() {
    if (isAdmin) {
        let elementsAdm = document.querySelectorAll('.admin-button');
        let elementsUsr = document.querySelectorAll('.user-button');
        elementsAdm.forEach((element) => {
            element.style.display = "inline";
        })
        elementsUsr.forEach((element) => {
            element.style.display = "none";
        })
    } else {
        updateReadyButton(isReady)
    }
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
function loadRoomUsers() {
    let callback = function (request) {
        clearUsersList(); // Отчищаем список пользователей, перед обновлением.
        const data = JSON.parse(request.responseText);
        data.forEach((user) => {
            showUser(user);
        });
        restoreSelectedUser();
    };
    sendRequest("GET", "/api/room/getUsersList", "", callback, [8]);
}

function clearUsersList() {
    userEntries.forEach((userEntry) => {
        userEntry["text"].innerText = "";
        userEntry["stamp-container"].innerText = "";
        userEntry["used"] = false;
        userEntry["login"] = "";
        userEntry["admin"] = false;
        userEntry["text"].classList.remove("clickable", "selected");
        userEntry["text"].onclick = "";
    });
}

/**
 * Добавляет пользователя в отображаемый на экране список
 * @param user -  пользователь.
 */
function showUser(user) {
    let currentEntry = null;
    for (let i = 0; i < userEntries.length; i++) {
        if (!userEntries[i]["used"]) {
            currentEntry = userEntries[i];
            break;
        }
    }
    currentEntry["used"] = true;
    currentEntry["login"] = user["name"];
    const textNodeName = document.createTextNode(user["name"]);
    currentEntry["text"].appendChild(textNodeName);
    if (!user["admin"]) {
        currentEntry["text"].classList.add("clickable");
        currentEntry["text"].onclick = function (event) {
            selectUser(event.target);
            updateButtonsOnSelect();
        }
    }
    if (user["admin"] || user["ready"]) {
        const stampNode = document.createElement("span");
        stampNode.classList.add("stamp");
        let textNodeStamp;
        if (user["admin"]) {
            currentEntry["admin"] = true;
            stampNode.classList.add("red");
            textNodeStamp = document.createTextNode("Админ");
        } else {
            stampNode.classList.add("blue");
            textNodeStamp = document.createTextNode("Готов");
        }
        stampNode.appendChild(textNodeStamp);
        // Случайный поворот от -5 до 5 градусов
        stampNode.style.transform = 'rotate(' + (Math.random() * (10) - 5) + 'deg) translateY(-0.5rem)';
        currentEntry["stamp-container"].appendChild(stampNode);
    }
}

function selectUser(node) {
    let login = node.innerText;
    const foundEntry = userEntries.find(element => element["login"] === login);
    if (!foundEntry["admin"]) {
        if (selectedEntry !== null) {
            selectedEntry["text"].classList.remove("selected");
            if (selectedEntry["login"] === login) {
                selectedEntry = null;
                return;
            }
            selectedEntry = null;
        }
        selectedEntry = foundEntry;
        foundEntry["text"].classList.add("selected");
    }
}

function updateButtonsOnSelect() {
    let button = document.getElementById("kick_user_button");
    button.disabled = selectedEntry == null;
}

function restoreSelectedUser() {
    if (selectedEntry !== null) {
        let login = selectedEntry["login"];
        const foundEntry = userEntries.find(element => element["login"] === login);
        if (foundEntry === null) {
            selectedEntry = null;
        } else {
            selectedEntry = foundEntry;
            foundEntry.classList.add("selected");
        }
    }
}


/**
 * Меняет состояние готовности пользователя.
 */
function changeUserReadyStatus() {

    let callback = function () {
        isReady = !isReady;
        updateReadyButton(isReady);
    };
    sendRequest("POST", "/api/room/setReady", !isReady, callback, [8]);

}


function updateReadyButton(isReady) {
    const button = document.getElementById("user_ready_button");
    if (!isReady) {
        button.innerText = "Я готов"
    } else {
        button.innerText = "Я не готов";
    }
}

function updateUsersReadiness(response) {
    loadRoomUsers();
    const data = JSON.parse(response.body);
    let button = document.getElementById("start_game_button");
    button.disabled = data;
}

