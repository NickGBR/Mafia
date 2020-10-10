let stompClient;

function connect() {

    if (
        // Проверяем поддержку WebSocket в бразуере. Первое условие проверяет наличие объекта
        // Второе проверяет его на соответствие стандарту, т. к. некоторые браузеры предоставляют
        // mock-реализацию. См. https://stackoverflow.com/a/40662794
        'WebSocket' in window && window.WebSocket.CLOSING === 2
    ) {
        const socket = new WebSocket("ws://localhost:8080/chat-messaging/websocket");
        stompClient = Stomp.over(socket);
    } else { // Используем  SockJS, если WebSocket не поддерживается
        const socket = new SockJS("http://localhost:8080/chat-messaging");
        stompClient = Stomp.over(socket);
    }

    stompClient.connect({}, function(frame) {
        console.log("connected: " + frame);
        stompClient.subscribe("http://localhost:8080/chat/messages", function(response) {
            const data = JSON.parse(response.body);
            draw("left", data.message);
        });
    });
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
}
function sendMessage(){
    stompClient.send("http://localhost:8080/app/message", {}, JSON.stringify({'message': $("#message_input_value").val()}));

}