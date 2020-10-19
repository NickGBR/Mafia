
let stompClient;
let name;
let storage = window.sessionStorage;
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
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    alert("Ошибка подключения к серверу.");
    console.log("Не удалось установить подключение: " + error);
    // Включаем кнопку подключения обратно.
    // Вдруг в следующий раз подключиться получиться
    setErrorDisconnectInterface()
}


/**
 * Метод установки имени пользователя пользователя в чате.
 */
function setName() {
    stompClient.subscribe()
    name = document.getElementById("name_input").value;

    const str = JSON.stringify({
        'message': {
            'from':name,
            'firstMessage':true
        }
    });
    stompClient.send("/app/system_message", {}, str);
    storage.setItem("name", name);
    document.location.href = "/roomList.html";
}
