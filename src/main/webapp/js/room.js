let room;

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

}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    console.log("Не удалось установить подключение: " + error);
    setErrorDisconnectInterface()
    document.getElementById("set_room_button").disabled = true;
}

// Передаем комнате, эллимент где будут отображаться все комнты.
function addRoom() {
    name = document.getElementById("room_input").value;
    const str = JSON.stringify({
        'name': name
    });

   // Отправляем название комнаты на сервер для ее создания.
    stompClient.send("/app/system_message", {}, str);
}