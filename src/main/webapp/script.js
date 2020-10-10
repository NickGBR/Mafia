let stompClient;

function connect() {

   // if (
        // Проверяем поддержку WebSocket в бразуере. Первое условие проверяет наличие объекта
        // Второе проверяет его на соответствие стандарту, т. к. некоторые браузеры предоставляют
        // mock-реализацию. См. https://stackoverflow.com/a/40662794
   //     'WebSocket' in window && window.WebSocket.CLOSING === 2
   // ) {
   //     const socket = new WebSocket("ws://localhost:8080/chat-messaging/websocket");
   //     stompClient = Stomp.over(socket);
   // } else { // Используем  SockJS, если WebSocket не поддерживается
        const socket = new SockJS("http://localhost:8080/chat-messaging");
        stompClient = Stomp.over(socket);
   // }

    stompClient.connect({}, function(frame) {
        console.log("connected: " + frame);
        stompClient.subscribe("http://localhost:8080/chat/messages", function(response) {
            const data = JSON.parse(response.body);
            console.log("Получено сообщение" + data.message);
            draw("left", data.message);
        });
    });
    // Включаем кнопки отправки\отключения и отключаем кнопку подключения
    document.getElementById("connect").disabled = true;
    document.getElementById("send").disabled = false;
    document.getElementById("disconnect").disabled = false;
}

function draw(side, text) {
    console.log("drawing...");
    let $message;
    $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.text').html(text);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);

}
function disconnect(){
    stompClient.disconnect();
    // Отключаем кнопки отправки\отключения и включаем кнопку подключения
    document.getElementById("connect").disabled = false;
    document.getElementById("send").disabled = true;
    document.getElementById("disconnect").disabled = true;
}
function sendMessage(){
    stompClient.send("http://localhost:8080/app/message", {},
        JSON.stringify({
            // Это работает на JQuery
            // 'message': $("#message_input_value").val()
            // А это на чистом JavaScript
            'message': document.getElementById("message_input_value").value
        }));

}