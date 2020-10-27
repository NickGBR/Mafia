const gameChatID = "game_chat_box";
let userName;
let userRole;
let roomName;
let roomID;
let isAdmin;
let isReady;
let destination;
let gamePhase;

let inputForm;
let voteButton;
let donCheckerButton;
let sheriffCheckerButton;
let sendMessageButton;


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

     inputForm = document.getElementById("message_input_value");
     voteButton = document.getElementById("vote_for_user_button");
     donCheckerButton = document.getElementById("don_checker");
     sheriffCheckerButton = document.getElementById("sheriff_checker");
     sendMessageButton = document.getElementById("send_message_button");

    roomName = initialisedRoomName;
    roomID = initialisedRoomID;
    userName = initialisedUserName;
    isAdmin = initialisedIsAdmin;
    isReady = initialisedIsReady;
    userRole = initialisedUserRole;
    gamePhase = init["gamePhase"];

    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено

    // Подписываемся на топики, в зависимости от роли.
    subscribeByRole();

    // Получаем информацию о текущем пользователе.
    showCurrentUserInfo();

    // Получаем историю чата.
    getUsersMessages();

    //Отключем все кнопки.
    disableInterface();

    setGamePhaseInterface(gamePhase);
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
        stompClient.subscribe(sockConst.SYS_WEB_CHAT + roomID, getGameStat);
    } else {
        console.log("Подписался как мирный на " + sockConst.CIV_WEB_CHAT);
        stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, getMessage);
        stompClient.subscribe(sockConst.SYS_WEB_CHAT + roomID, getGameStat);

    }
}

/**
 * Меняет интерфейс игры, в зависимоти от игровой фазы.
 * @param phase - фаза игры.
 */
function setGamePhaseInterface(phase) {
    disableInterface();
    switch (phase) {
        case gamePhaseConst.CIVILIANS_DISCUSS_PHASE:
            destination = destinationConst.CIVILIAN;
            activateCivilianDiscussInterface()
            break;

        case gamePhaseConst.CIVILIANS_VOTE_PHASE:
            activateCiviliansVotingInterface()
            break;

        case gamePhaseConst.MAFIA_DISCUSS_PHASE:
            destination = destinationConst.MAFIA;
            if (userRole === roleConst.MAFIA ||
                userRole === roleConst.DON)
                activateMafiaDiscussInterface();
            break;

        case  gamePhaseConst.MAFIA_VOTE_PHASE:
            activateMafiaVotingInterface();
            break;

        case gamePhaseConst.DON_PHASE:
            if (userRole === roleConst.DON)
                activateDonInterface();
            break;

        case gamePhaseConst.SHERIFF_PHASE:
            if (userRole === roleConst.SHERIFF) activateSheriffInterface();
            activateSheriffInterface();
            break;

        default:
            alert("Проблемы с фазой игры, обратитесь к разработчику.");
    }
}

function disableInterface(){
    console.log(voteButton);
    voteButton.disabled = true;
    sendMessageButton.disabled = true;
    inputForm.disabled = true;
    sheriffCheckerButton.disabled = true;
    donCheckerButton.disabled = true;
    sheriffCheckerButton.style.display = "none";
    donCheckerButton.style.display = "none"

}

function activateCivilianDiscussInterface() {
    sendMessageButton.disabled = false;
    inputForm.disabled = false;
}

function activateCiviliansVotingInterface() {
    inputForm.disabled = false;
    inputForm.placeHolder = "Введите логин Мафии!";
    voteButton.disabled = false;
}

function activateMafiaDiscussInterface() {
    sendMessageButton.disabled = false;
    inputForm.disabled = false;
}

function activateMafiaVotingInterface() {
voteButton.disabled = false;
inputForm.placeHolder = "Кого желаете убить!";
inputForm.disabled = false;
}

function activateDonInterface() {
    donCheckerButton.style.display = "block";
    donCheckerButton.disabled = false;
    inputForm.disabled = false;
}

function activateSheriffInterface() {
    sheriffCheckerButton.style.display = "block";
    sheriffCheckerButton.disabled = false;
    inputForm.disabled = false;
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
function getMessage(response) {
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
    sendRequest("POST", sockConst.REQUEST_POST_MESSAGE, data, callback, [8]);
}

/**
 * Отправляем серверу название комнаты, для получения истории чата.
 */
function getUsersMessages() {

    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        data.forEach((message) => {
            if (message['destination'] === destinationConst.CIVILIAN) {
                addToChat(message["from"] + ": " + message["text"], gameChatID);
            }
        });
    };

    sendRequest("GET", "/api/message/restore", "", callback, [8]);
}

function getGameStat(response) {

    const data = JSON.parse(response.body);
    if (data["gamePhase"] === gamePhaseConst.END_GAME_PHASE) {
        showEndGameScreen(data['message']);
        return;
    }


    setGamePhaseInterface(data['gamePhase']);
    if (data['message'] !== 0) {
        addToChat(data["message"], gameChatID)
    }
}

/**
 * Функция для шерифа и дона, проверяющая является ли тот или иной игрок
 * доном или шерифом. Проверка на дона или шерифа, происходит на сервере
 * автоматически, так как мы знаем роли игрока, сделавшего запрос.
 */
function checkUserRole(){
    let callback = function (request) {

        console.log(request.responseText);
    };
    let login = document.getElementById("message_input_value").value;
    sendRequest("GET", sockConst.REQUEST_GET_ROLE_INFO +"?login=" + login, "", callback, [8]);
}

function voteForUser(){
    let callback = function (request) {

        console.log(request.responseText);
    };
    let login = document.getElementById("user_login_value").value;
    sendRequest("GET", sockConst.REQUEST_GET_ROLE_INFO +"?login=" + login, "", callback, [8]);
}

function showEndGameScreen(message) {
    showModalMessage("Игра окончена",
        message,
        function () {
            window.location.href = "roomList.html";
        });
}

function getLog() {
    console.log(userRole);
}

