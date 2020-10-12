
function connect() {
    var socket = new SockJS("/chat-messaging");
    stompClient = Stomp.over(socket);
<<<<<<< Updated upstream
    stompClient.connect({}, function(frame) {
        console.log("connected: " + frame);
        stompClient.subscribe("/chat/messages", function(response) {
            var data = JSON.parse(response.body);
            draw("left", data.message);
        });
    });
}

function draw(side, text) {
    console.log("drawing...");
    var $message;
    $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.text').html(text);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);

=======

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
    stompClient.subscribe("/chat/civ_messages", getMessage);// {

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

function getMessage(response){
    const data = JSON.parse(response.body);
    console.log("Получено сообщение: " + data.message);
    addToChat(data.message);
}


function addToChat(text) {
    const node = document.createElement("LI");  // Создаем элемент списка <li>
    node.setAttribute("class", "message");
    const textNode = document.createTextNode(text);  // Создаем текстовый элемент
    node.appendChild(textNode); // Вставляем текстовый внутрь элемента списка
    document.getElementById("chatbox_civilians").appendChild(node); // А элемент внутрь самого списка
>>>>>>> Stashed changes
}
function disconnect(){
    stompClient.disconnect();
<<<<<<< Updated upstream
}
function sendMessage(){
    stompClient.send("/app/message", {}, JSON.stringify({'message': $("#message_input_value").val()}));
=======
    // Отключаем кнопки отправки\отключения и включаем кнопку подключения
    document.getElementById("connect").disabled = false;
    document.getElementById("send_civilians").disabled = true;
    document.getElementById("send_mafia").disabled = true;
    document.getElementById("disconnect").disabled = true;
   // document.getElementById()
}

function sendMessage() {
    stompClient.send("/app/message", {},
        JSON.stringify({
            // Это работает на JQuery
            // 'message': $("#message_input_value").val()
            // А это на чистом JavaScript
            'message': document.getElementById("message_input_civilians_value").value
        }));
>>>>>>> Stashed changes

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

