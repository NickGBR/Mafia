let stompClient;


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
    stompClient.subscribe("/chat/messages", function (response) {
        const data = JSON.parse(response.body);
        console.log("Получено сообщение: " + data.message);
        addToChat(data.message);
    });
    // Теперь когда подключение установлено
    // Включаем кнопки для отправки сообщений и отключения от сервера
    document.getElementById("send").disabled = false;
    document.getElementById("disconnect").disabled = false;
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    // Включаем кнопку подключения обратно.
    // Вдруг в следующий раз подключиться получиться
    document.getElementById("connect").disabled = false;
}


function addToChat(text) {
    const node = document.createElement("LI");  // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);  // Создаем текстовый элемент
    node.appendChild(textNode); // Вставляем текстовый внутрь элемента списка
    document.getElementById("messages-list").appendChild(node); // А элемент внутрь самого списка
}

function disconnect() {
    stompClient.disconnect();
    // Отключаем кнопки отправки\отключения и включаем кнопку подключения
    document.getElementById("connect").disabled = false;
    document.getElementById("send").disabled = true;
    document.getElementById("disconnect").disabled = true;
}

function sendMessage() {
    stompClient.send("/app/message", {},
        JSON.stringify({
            // Это работает на JQuery
            // 'message': $("#message_input_value").val()
            // А это на чистом JavaScript
            'message': document.getElementById("message_input_value").value
        }));

}