function readCookie(name) {
    let nameEQ = name + '=',
        allCookies = document.cookie.split(';'),
        i,
        cookie;
    for (i = 0; i < allCookies.length; i += 1) {
        cookie = allCookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1, cookie.length);
        }
        if (cookie.indexOf(nameEQ) === 0) {
            return cookie.substring(nameEQ.length, cookie.length);
        }
    }
    return null;
}

let stompClient;

function connect() {
    // Подключается через SockJS. Он сам решит использовать ли WebSocket
    // или имитировать их другими средствами
    const socket = new SockJS("http://localhost:8080/chat-messaging");
    console.log("Connected successfully");
    stompClient = Stomp.over(socket);

    // Извлекаем наш JWT из куки
    const token = readCookie("token");
    console.log("Current token: " + token);

    // Пытаемся подключиться, передавая заголовок для аутентификации.
    // Имя заголовка не стандартизовано и должно просто соотвествовать имени ожидаемому в WebSocketConfiguration
    // И две функции: одну для обработки успешного подключения,
    // и вторую для обработки ошибки подключения
    stompClient.connect({'x-auth-token': token}, afterConnect, onError);
    // Отключаем кнопку подключения, чтобы пользователь не
    // начинал несколько попыток подключения за раз
    // document.getElementById("connect_button").disabled = true;
}

// Будем вызвано после установления соединения
function afterConnect(connection) {
    console.log("Успешное подключение: " + connection);
}

// Будет вызвано при ошибке установления соединения
function onError(error) {
    // К сожалению Spring Security просто кидает исключение, так что ищем текст исключения
    // в возвращенной ошибке, вместо более элегантной проверки
    if (error.message.indexOf("Access is denied")) {
        console.log("Не удалось установить подключение: отказано в доступе");
    } else {
        console.log("Не удалось установить подключение: " + error);
    }
    // Включаем кнопку подключения обратно.
    // Вдруг в следующий раз подключиться получиться
    // setErrorDisconnectInterface()
}

