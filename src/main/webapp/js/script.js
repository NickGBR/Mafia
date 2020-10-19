let civiliansChat = "civilians_chat_box"; // Хранит id HTML елемента отвечаючего за отображение сообщений в чате мирных жителей.
let mafiaChat = "mafia_chat_box";         // Хранит id HTML елемента отвечаючего за отображение сообщений в чате мафии.
let stompClient;
let name;
let isMafia = false;
let isNight;
let room;
let isGameActive = false;
let storage = window.sessionStorage;

function getData(){
    console.log(storage.getItem("name"))
}
function connect() {
    // Подключается через SockJS. Он сам решит использовать ли WebSocket
    // или имитировать их другими средствами
    const socket = new SockJS("http://localhost:8080/chat-messaging");
    console.log("Connected successfully");
    stompClient = Stomp.over(socket);

    // Пытаемся подключиться, передавая пустой список заголовков - {}
    // И две функции: одну для обработки успешного подключения,
    // и вторую для обработки ошибки подключения
    stompClient.connect({}, afterConnect, onError);
    // Отключаем кнопку подключения, чтобы пользователь не
    // начинал несколько попыток подключения за раз
    //document.getElementById("connect_button").disabled = true;
}
// Будем вызвано после установления соединения
function afterConnect(connection) {
    console.log("Успешное подключение: " + connection);
    // Теперь когда подключение установлено
    // Включаем кнопки для отправки сообщений и отключения от сервера
    document.getElementById("set_name_button").disabled = false;
    document.getElementById("set_room_button").disabled = false;

    //document.getElementById("disconnect_button").disabled = false;

}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    alert("Ошибка подключения к серверу.");
    console.log("Не удалось установить подключение: " + error);
    // Включаем кнопку подключения обратно.
    // Вдруг в следующий раз подключиться получиться
    setErrorDisconnectInterface()
}

//Получает информацию об игре с сервера.
function getStat(response) {
    const data = JSON.parse(response.body);
    isNight = data.night;
    console.log(isNight);
    setTimeInterface(isNight);
    //Отправляем сообщение о времени суток
    sendTimeMessage(isNight);
}

// Данный метод получает сообщения от сокета.
function getMessage(response) {
    const data = JSON.parse(response.body);
    console.log("Получено сообщение: " + data.text)
    console.log("Название чата: " + data.role);
    console.log("From = " + data.from)

    //Определяем от кого пришло сообщение.
    if (data.role === "MAFIA") addToChat(data.from + ": " + data.text, mafiaChat);
    else if (data.role === "CIVILIAN")
        addToChat(data.from + ": " + data.text, civiliansChat);
    else if (data.role === "HOST") addToChat(data.from + ": " + data.text, civiliansChat);
}

// Добавление сообщения в HTML
function addToChat(text, chat) {
    const node = document.createElement("LI");      // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);         // Создаем текстовый элемент
    node.appendChild(textNode);                             // Вставляем текстовый внутрь элемента списка
    document.getElementById(chat).appendChild(node);
}

function addRoomToInterface(room, elementId){
    const dd = document.createElement("dd")           // Создаем элемент списка <dd>
    const button = document.createElement("button")     // Создаем кнопку
    button.setAttribute("id", room);                 // Устанавливаем id как название комнаты
    button.setAttribute("onclick","alert(this.id)"); // Действие при нажатии кнопки
    const buttonName = document.createTextNode(room);                 // Создаем текстовый элемент

    button.appendChild(buttonName);
    dd.appendChild(button)// Вставляем текстовый внутрь элемента списка
    document.getElementById(elementId).appendChild(dd);
}

function disconnect() {
    stompClient.disconnect();
    // Отключаем кнопки отправки\отключения и включаем кнопку подключения
    setErrorDisconnectInterface();
}

// chat - HTML эллимент для отправки сообщения.
// view - HTML эллимент откуда берется сообщение.
function sendMessage(chat, view) {
    const str = JSON.stringify({
        // Это работает на JQuery
        // 'message': $("#message_input_value").val()
        // А это на чистом JavaScript
        'room': room,                                    // Сервер должен знать в какую комнату переслать сообщение.
        'text': document.getElementById(view).value,
        'from': name
    });
    console.log(str);
    stompClient.send(chat, {}, str);
}

function getStorageData(){
    name = storage.getItem("name");
}
function sendSystemMessage(chat, view) {
    const str = JSON.stringify({
        // Это работает на JQuery
        // 'message': $("#message_input_value").val()
        // А это на чистом JavaScript
        'room': room,                                    // Сервер должен знать в какую комнату переслать сообщение.
        "message": {
            'text': document.getElementById(view).value,
            'role': "HOST",
            'from' : "Host"
        }
    });
    stompClient.send(chat, {}, str);
}

/**
 * Метод установки имени пользователя пользователя в чате.
 */
function setName() {
    name = document.getElementById("name_input").value;
    storage.setItem("name", name);
    document.location.href = "/roomList.html";
}

/**
 * Передаем комнате, эллимент где будут отображаться все комнты.
  * @param elementId
 */
function addRoom(elementId) {
    room = document.getElementById("room_input").value;
    //document.getElementById("mafia_role_button").disabled = false;
    //document.getElementById("civilians_role_button").disabled = false;
    //document.getElementById("set_room_button").disabled = true;
    //document.getElementById("room_input").disabled = true;
    const str = JSON.stringify({
        'room': room
    });
    // Заранее отправляем информацию о добавлении новой комнаты на сервер
    // чтобы телеграм пользователи могу в нее войти до начала игры.
    stompClient.send("/app/system_message", {}, str);
    addRoomToInterface(room,elementId)
}

/**
 * Метод устанавливающий начальную конфигурацию пользовательского интерфейса в зависимости от роли игрока.
 * @param isMafia
 */
function setRoleInterface(isMafia) {
    console.log(isMafia)
    if (isMafia === true) {
        document.getElementById("mafia_chat_button").style.visibility = "visible";
        document.getElementById("civilians_chat_button").style.visibility = "visible";
        document.getElementById("civilians_chat_button").click();
    } else {
        document.getElementById("civilians_chat_button").style.visibility = "visible";
        document.getElementById("civilians_chat_button").click();
    }
}

/**
 * Метод выводящий сообщение в зависимости от времени в игре.
 * @param isNight
 */
function sendTimeMessage(isNight) {
    if (isNight === false) {
        addToChat("День настал!", mafiaChat);
        addToChat("День настал!", civiliansChat);
    }
    if (isNight === true) {
        addToChat("Ночь настала!", mafiaChat);
        addToChat("Ночь настала!", civiliansChat);

    }
}

/**
 * Метод меняющий чат в зависимости от времени в игре.
 * @param isNight
 */
function setTimeInterface(isNight) {
    if (isNight === true) {
        //document.body.style.backgroundColor = "#03133C";
        document.getElementById("send_mafia_button").disabled = false;
        document.getElementById("send_civilians_button").disabled = true;
    } else{
        //document.body.style.backgroundColor = "#FFFFFF";
        document.getElementById("send_mafia_button").disabled = true;
        document.getElementById("send_civilians_button").disabled = false;
    }
}

/**
 * Метод для выбора роли при помощи кнопок.
 * @param id
 */
function chooseRole(id) {
    if (id === "mafia_role_button") {
        isMafia = true;
        stompClient.subscribe("/chat/civ_messages/" + room, getMessage);
        stompClient.subscribe("/chat/mafia_messages/" + room, getMessage)
    } else if (id === "civilians_role_button") {
        isMafia = false;
        stompClient.subscribe("/chat/civ_messages/" + room, getMessage);
    }

    document.getElementById("start_game_button").disabled = false;
    document.getElementById("mafia_role_button").disabled = true;
    document.getElementById("civilians_role_button").disabled = true;
    setRoleInterface(isMafia);
}

function startGame(chat) {
    sendStartGameMessage(chat);
    //  Подписываемя на топик информации об игре, передаем метод для обработки ответа, устанавливаем id для
    // возможноти отписаться от топика, если не отписаться, клиен подписывается на один и тот-же топик два раза
    // и сообщения дублируются.
    stompClient.subscribe("/chat/game_stat/" + room, getStat, {id:"game_stat_chat"});

    document.getElementById("start_game_button").disabled = true;
    document.getElementById("stop_game_button").disabled = false;
    document.getElementById("send_host_button").disabled = false;
}

function sendStartGameMessage(chat) {
    const str = JSON.stringify({
        'room': room,
    });
    stompClient.send(chat, {}, str);
}

function stopGame(chat) {
    sendStopGameMessage(chat);
    console.log(stompClient.unsubscribe("game_stat_chat") + "!!!!!!!");
    document.getElementById("start_game_button").disabled = false;
    document.getElementById("stop_game_button").disabled = true;
    document.getElementById("send_host_button").disabled = true;
}

function sendStopGameMessage(chat) {
    const str = JSON.stringify({
        'interrupted': true,
        'room': room
    });
    stompClient.send(chat, {}, str);
}

/**
 * Настойка интерфеса при отключении от сервера или ошибке.
  */
function setErrorDisconnectInterface() {
    document.getElementById("send_civilians_button").disabled = true;
    document.getElementById("send_mafia_button").disabled = true;
    document.getElementById("disconnect_button").disabled = true;
    document.getElementById("set_name_button").disabled = true;
    document.getElementById("set_room_button").disabled = true;
    document.getElementById("start_game_button").disabled = true;
    document.getElementById("stop_game_button").disabled = true;
    document.getElementById("connect_button").disabled = false;
    document.getElementById("room_input").disabled = false;
    document.getElementById("name_input").disabled = false;

}

function openCity(evt, cityName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}