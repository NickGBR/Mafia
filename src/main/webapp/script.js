let stompClient;
let mafia = false;
let name;

function connect() {
    // Подключается через SockJS. Он сам решит использовать ли WebSocket
    // или имитировать их другими средствами
    const socket = new SockJS("http://localhost:8080/chat-messaging");
    stompClient = Stomp.over(socket);

    // Пытаемся подключиться, передавая пустой список заголовков - {}
    // И две функции: одну для обработки успешного подключения,
    // и вторую для обработки ошибки подключения
    stompClient.connect({}, afterConnect, onError);
    // Отключаем кнопку подключения, чтобы пользователь не
    // начинал несколько попыток подключения за раз
    document.getElementById("connect").disabled = true;
}

// Будем вызвано после установления соединения
function afterConnect(connection) {
    console.log("Успешное подключение: " + connection);
    stompClient.subscribe("/chat/civ_messages", getMessage);
    stompClient.subscribe("/chat/mafia_messages", getMessage);
    stompClient.subscribe("/chat/game_stat", getStat);
    // Теперь когда подключение установлено
    // Включаем кнопки для отправки сообщений и отключения от сервера
    document.getElementById("send_mafia").disabled = false;
    document.getElementById("send_civilians").disabled = false;
    document.getElementById("disconnect").disabled = false;
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    // Включаем кнопку подключения обратно.
    // Вдруг в следующий раз подключиться получиться
    document.getElementById("connect").disabled = false;
}

function getStat(response) {
    const data = JSON.parse(response.body);
    const night = data.isNight;
    console.log("Lolka");
    if (night == false) console.log("Сейчас ночь.");
    else console.log("Сейчам день.")
    addToChat(night);
}

function getMessage(response) {
    const data = JSON.parse(response.body);
    console.log("Получено сообщение: " + data.message);
    console.log("Роль: " + data.role);
    console.log("From = " + data.from)
    if (data.role != "MAFIA") {
        //document.getElementById("mafia_chat").style.display = "none";
    }
    addToChat(data.from + " " + data.message);
}


function addToChat(text) {
    const node = document.createElement("LI");  // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);  // Создаем текстовый элемент
    node.appendChild(textNode); // Вставляем текстовый внутрь элемента списка
    if (mafia == true) document.getElementById("chatbox_mafia").appendChild(node);
    else
        document.getElementById("chatbox_civilians").appendChild(node); // А элемент внутрь самого списка
}

function disconnect() {
    stompClient.disconnect();
    // Отключаем кнопки отправки\отключения и включаем кнопку подключения
    document.getElementById("connect").disabled = false;
    document.getElementById("send_civilians").disabled = true;
    document.getElementById("send_mafia").disabled = true;
    document.getElementById("disconnect").disabled = true;
    // document.getElementById()
}

function sendMessage(chat, view) {
    const str = JSON.stringify({
        // Это работает на JQuery
        // 'message': $("#message_input_value").val()
        // А это на чистом JavaScript
        'message': document.getElementById(view).value,
        'from': name

    });
    console.log(str);
    stompClient.send(chat, {}, str);
    if (document.getElementById(view).id == "message_input_mafia_value") mafia = true;
    else mafia = false;
}

function setName() {
    name = document.getElementById("name_input").value;
    console.log("Name " + name + " set!");
    document.getElementById("set_name").disabled = true;
    document.getElementById("name_input").disabled = true;

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

