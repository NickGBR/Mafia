let userName;
let userRole;
let roomName;
let roomID;
let isAdmin;
let isReady;
let destination;
let gamePhase;
let maxUserAmount;

let selectedEntry = null;
const charactersListId = "characters_list";
let characterEntries = [];


let sendMessageButton;

let voteButton;
let allowedVoteButton;
let donCheckerButton;
let allowedDonCheckerButton;
let sheriffCheckerButton;
let allowedSheriffCheckerButton;


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

    voteButton = document.getElementById("vote_for_user_button");
    donCheckerButton = document.getElementById("don_checker");
    sheriffCheckerButton = document.getElementById("sheriff_checker");

    roomName = initialisedRoomName;
    roomID = initialisedRoomID;
    userName = initialisedUserName;
    isAdmin = initialisedIsAdmin;
    isReady = initialisedIsReady;
    userRole = initialisedUserRole;
    gamePhase = init["gamePhase"];
    maxUserAmount = init["maxUserAmount"];
    sendMessageButton = document.getElementById("send_message_button");
    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено

    // Подписываемся на топики, в зависимости от роли.
    subscribeByRole();

    // Заполняем информацию об игре.
    initCurrentGameInfo();

    // Получаем историю чата.
    loadChatMessages();

    //Получаем список пользователей
    loadGameCharacters();

    //Отключем все кнопки.
    disableInterface();

    setGamePhaseInterface(gamePhase);
}

function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    showModalMessage("Ошибка", "Клиент потерял соединение с сервером");
    document.getElementById("left_room_button").disabled = true;
    document.getElementById("user_ready_button").disabled = true;
}

/**
 * Подписывает пользователей на топики, в зависимости от роли в игре.
 */
function subscribeByRole() {
    stompClient.subscribe(sockConst.CIV_WEB_CHAT + roomID, receiveMessage);
    stompClient.subscribe(sockConst.SYS_WEB_CHAT + roomID, getGameStat);
    if (userRole === roleConst.MAFIA || userRole === roleConst.DON) {
        console.log("Подписался как мафия");
        stompClient.subscribe(sockConst.MAFIA_WEB_CHAT + roomID, receiveMafiaMessage)
    } else {
        console.log("Подписался как мирный на " + sockConst.CIV_WEB_CHAT);
    }
}

/**
 * Инициализирует поля веб-страницы информацией об игре
 */
function initCurrentGameInfo() {
    updateGameDescription();
    let firstNode = document.getElementById("character_entry_1");
    let userEntry = initCharacterEntry(firstNode);
    characterEntries.push(userEntry);
    for (let i = 1; i < maxUserAmount; i++) {
        const copyNode = firstNode.cloneNode(true);
        copyNode.id = "user_entry_" + (i + 1);
        document.getElementById(charactersListId).appendChild(copyNode);
        let userEntry = initCharacterEntry(copyNode);
        characterEntries.push(userEntry);
    }
    console.log(characterEntries);
}

function initCharacterEntry(node) {
    let userEntry = {};
    userEntry["name"] = "";
    userEntry["isAlive"] = false;
    userEntry["role"] = "";
    userEntry["stamp-container"] = node.querySelector('.stamp-container');
    userEntry["text"] = node.querySelector('.text');
    return userEntry;
}

function updateGameDescription() {
    let description = "Вы \u2014";
    switch (userRole) {
        case roleConst.DON: {
            description = description + " Дон. Убейте всех мирных и найдите шерифа.";
            break;
        }
        case roleConst.MAFIA: {
            description = description + " мафия. Убейте всех мирных.";
            break;
        }
        case roleConst.SHERIFF: {
            description = description + " Шериф. Ищите мафию по ночам и голосуйте против них днем.";
            break;
        }
        case roleConst.CITIZEN: {
            description = description + " Мирный житель. Голосуйте против мафии днем.";
        }
    }
    description = description + " Сейчас: ";
    switch (gamePhase) {
        case gamePhaseConst.CIVILIANS_DISCUSS_PHASE:
            description = description + " День. Обсудите, кто из вас может быть мафией.";
            break;
        case gamePhaseConst.CIVILIANS_VOTE_PHASE:
            description = description + " День. Голосуйте против того, кого считайте мафией.";
            break;
        case gamePhaseConst.MAFIA_DISCUSS_PHASE:
            description = description + " Ночь. Мафия обсуждает коварные планы";
            break;
        case gamePhaseConst.MAFIA_VOTE_PHASE:
            description = description + " Ночь. Мафия голосует, кого убить.";
            break;
        case gamePhaseConst.DON_PHASE:
            description = description + " Ночь. Дон ищет шерифа.";
            break;
        case gamePhaseConst.SHERIFF_PHASE:
            description = description + " Ночь. Шериф ищет мафию";
            break;
        default:
            showModalMessage("Ошибка", "Проблемы с фазой игры, обратитесь к разработчику.")
    }
    document.getElementById("game-description").innerText = description;
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
            showModalMessage("Ошибка", "Проблемы с фазой игры, обратитесь к разработчику.")
    }
}

function disableInterface() {
    console.log(voteButton);
    allowedVoteButton = false;
    allowedDonCheckerButton = false;
    allowedSheriffCheckerButton = false;
    sendMessageButton.disabled = true;
    updateButtonOnStateChange();
    sheriffCheckerButton.style.display = "none";
    donCheckerButton.style.display = "none"
}

function activateCivilianDiscussInterface() {
    sendMessageButton.disabled = false;
}

function activateCiviliansVotingInterface() {
    allowedVoteButton = true;
    updateButtonOnStateChange();
}

function activateMafiaDiscussInterface() {
    sendMessageButton.disabled = false;
}

function activateMafiaVotingInterface() {
    allowedVoteButton = true;
    updateButtonOnStateChange();
}

function activateDonInterface() {
    donCheckerButton.style.display = "block";
    allowedDonCheckerButton = true;
    updateButtonOnStateChange();
}

function activateSheriffInterface() {
    sheriffCheckerButton.style.display = "block";
    allowedSheriffCheckerButton = true;
    updateButtonOnStateChange();
}


/**
 * Отправляет сообщения пользователей в end point.
 */
function sendMessageToDestination() {
    sendMessage(destination);
}


function getGameStat(response) {
    const data = JSON.parse(response.body);
    if (data["gamePhase"] === gamePhaseConst.END_GAME_PHASE) {
        showEndGameScreen(data['message']);
        return;
    }
    gamePhase = data['gamePhase'];
    setGamePhaseInterface(data['gamePhase']);
    updateGameDescription();
    if (data['message'] !== 0) {
        receiveSystemMessage(data["message"]);
    }
}

/**
 * Функция для шерифа и дона, проверяющая является ли тот или иной игрок
 * доном или шерифом. Проверка на дона или шерифа, происходит на сервере
 * автоматически, так как мы знаем роли игрока, сделавшего запрос.
 */
function checkUserRole() {
    let callback = function (request) {

        console.log(request.responseText);
    };
    let login = selectedEntry["name"];
    sendRequest("GET", sockConst.REQUEST_GET_ROLE_INFO + "?login=" + login, "", callback, [8]);
}

function voteForUser() {
    let login = selectedEntry["name"];
    sendRequest("GET", sockConst.REQUEST_GET_VOTE_FOR_USER + "?login=" + login, "", null, [8]);
}

function showEndGameScreen(message) {
    showModalMessage("Игра окончена",
        message,
        function () {
            window.location.href = "roomList.html";
        });
}


/**
 * Отвечает за получение персонажей при заходе в игру.
 */
function loadGameCharacters() {
    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        data.forEach((character) => {
            showCharacter(character);
        });
    };
    sendRequest("GET", "/api/game/getCharacters", "", callback, [3, 8, 16]);
}

/**
 * Добавляет персонажа в отображаемый на экране список
 * @param character -  персонаж.
 */
function showCharacter(character) {
    let currentEntry = null;
    for (let i = 0; i < characterEntries.length; i++) {
        if (!characterEntries[i]["used"]) {
            currentEntry = characterEntries[i];
            break;
        }
    }
    currentEntry["used"] = true;
    currentEntry["name"] = character["name"];
    const textNodeName = document.createTextNode(character["name"]);
    currentEntry["text"].appendChild(textNodeName);
    if (character["name"] !== userName && character["isAlive"]) {
        currentEntry["text"].classList.add("clickable");
        currentEntry["text"].onclick = function (event) {
            selectCharacter(event.target);
            updateButtonOnStateChange();
        }
    }
    if (!character["isAlive"]) {
        const strokeNode = currentEntry["text"].querySelector('.strikethrough');
        strokeNode.style.visibility = "visible";
    }
    if (character["role"] === roleConst.MAFIA || character["role"] === roleConst.DON) {
        const stampNode = document.createElement("span");
        stampNode.classList.add("stamp");
        let textNodeStamp;
        stampNode.classList.add("red");
        if (character["role"] === roleConst.DON) {
            textNodeStamp = document.createTextNode("Дон");
        } else {
            textNodeStamp = document.createTextNode("Мафия");
        }
        stampNode.appendChild(textNodeStamp);
        // Случайный поворот от -5 до 5 градусов
        stampNode.style.transform = 'rotate(' + (Math.random() * (10) - 5) + 'deg) translateY(-0.5rem)';
        currentEntry["stamp-container"].appendChild(stampNode);
    }
}

function selectCharacter(node) {
    let name = node.innerText;
    if (name !== userName) {
        const foundEntry = characterEntries.find(element => element["name"] === name);
        if (selectedEntry !== null) {
            selectedEntry["text"].classList.remove("selected");
            if (selectedEntry["name"] === name) {
                selectedEntry = null;
                return;
            }
            selectedEntry = null;
        }
        selectedEntry = foundEntry;
        foundEntry["text"].classList.add("selected");
    }
}

function updateButtonOnStateChange() {
    voteButton.disabled = !allowedVoteButton || (selectedEntry == null);
    sheriffCheckerButton.disabled = !allowedSheriffCheckerButton || (selectedEntry == null);
    donCheckerButton.disabled = !allowedDonCheckerButton || (selectedEntry == null);
}
