let openChat = false;
const roomChatId = "room_chat_box";

function switchChat() {
    const area = document.getElementById("chat_area");
    const header = document.getElementById("header");
    const content = document.getElementById("content");
    const overlay = document.getElementById("overlay-chat");
    const divider = document.getElementById("divider");
    if (!openChat) {
        header.style.height = "19.5rem";
        header.style.paddingBottom = "0";
        content.style.marginTop = "21.0rem";
        area.style.display = "block";
        overlay.style.display = "block";
        divider.style.borderWidth = "0 0 0 0";
        divider.style.paddingBottom = "0";
    } else {
        area.style.display = "none";
        content.style.marginTop = "4rem";
        header.style.height = "2.5rem";
        header.style.paddingBottom = "0.5rem";
        overlay.style.display = "none";
        divider.style.borderWidth = "0 0 2px 0";
        divider.style.paddingBottom = "0.5rem";
    }
    openChat = !openChat;
}

/**
 * Отправляем серверу название комнаты, для получения истории чата.
 */
function loadChatMessages() {

    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        const chatbox = document.getElementById(roomChatId);
        data.slice().reverse().forEach((message) => {
            const div = convertMessageToDiv(message);
            chatbox.insertBefore(div, chatbox.firstChild);
        });
    };
    sendRequest("GET", "/api/message/restore", "", callback, [8]);
}

/**
 * Отправляет сообщения пользователей в end point.
 */
function sendMessage() {
    let message = document.getElementById("message_input_value").value;
    document.getElementById("message_input_value").value = "";
    message = message.trim();
    const data = JSON.stringify({
        'text': message,
        'destination': destinationConst.ROOM_USER
    })

    if (message !== "") {
        let callback = function (request) {
        };
        sendRequest("POST", "/api/message/send",
            data, callback, [8]);
    }
}

function receiveMessage(response) {
    const data = JSON.parse(response.body);
    // Отправляем сообщение в чат
    let div = convertMessageToDiv(data);
    document.getElementById(roomChatId).appendChild(div);
}

/**
 * Добавляет сообщение в окно чата
 * @param message - сообщение
 */
function convertMessageToDiv(message) {
    console.log(message);
    const divNode = document.createElement("DIV");      // Создаем <div>
    const fromNode = document.createElement("SPAN");      // Создаем поле отправителя
    fromNode.classList.add("from");
    fromNode.innerText = message["from"] + ": ";
    const textNode = document.createElement("SPAN");      // Создаем поле текста
    textNode.innerText = message["text"];
    divNode.appendChild(fromNode);                             // Вставляем отправителя в div
    divNode.appendChild(textNode);                             // Вставляем текст в div
    return divNode
}