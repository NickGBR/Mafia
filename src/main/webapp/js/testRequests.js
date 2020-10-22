function getUserName() {

    let callback = function (request) {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Имя пользователя: " + request.responseText);
        output.appendChild(textNode)
    };

    sendRequest("GET", "/api/user/getCurrentName", "", callback, []);

}

function createRoom() {

    let callback = function () {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Комната создана");
        output.appendChild(textNode)
    };
    const jsonData = {
        'name': 'testRoom',
        'description': 'This is test room'
    };
    sendRequest("POST", "/api/room/create", JSON.stringify(jsonData), callback, [5]);
}

function disbandRoom() {

    let callback = function () {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Комната расформирована");
        output.appendChild(textNode)
    };

    sendRequest("POST", "/api/room/disband", "", callback, [8, 9]);

}

let roomId = -1;

function getRooms() {

    let callback = function (request) {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const data = JSON.parse(request.responseText);
        roomId = -1;
        data.forEach(function (item, index) {
            const textNode = document.createTextNode("Имя комнаты: " + item.name);
            const node = document.createElement("LI");      // Создаем элемент списка <li>
            node.appendChild(textNode)
            output.appendChild(node)
            if (index === 2) {
                roomId = item.id;
            }
        });
    };

    sendRequest("GET", "/api/room/getInitialList", "", callback, []);
}

function joinRoom() {

    if (roomId < 0) {
        alert("Не получен список комнат или в нем нет тестовой комнаты.");
        return;
    }

    let callback = function () {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Вход выполнен успешно");
        output.appendChild(textNode)
    };
    const jsonData = {
        'id': roomId,
        'password': ''
    };
    sendRequest("POST", "/api/room/join", JSON.stringify(jsonData), callback, [4, 5, 10, 11, 12]);
}

function leaveRoom() {

    let callback = function () {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Выход выполнен успешно");
        output.appendChild(textNode)
    };

    sendRequest("POST", "/api/room/leave", "", callback, [8, 11]);
}

let userLogin = "";

function getUsers() {
    let callback = function (request) {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const data = JSON.parse(request.responseText);
        userLogin = ""
        data.forEach(function (item) {
            const textNode = document.createTextNode("Имя пользователя: " + item["name"]
                + ", Админ: " + item["admin"] + ", Готов: " + item["ready"]);
            const node = document.createElement("LI");      // Создаем элемент списка <li>
            node.appendChild(textNode)
            output.appendChild(node)
            if (item["admin"] === false) {
                userLogin = item["name"];
            }
        });
    };

    sendRequest("GET", "/api/room/getUsersList", "", callback, [8]);
}

function kickUser() {
    if (userLogin === "") {
        alert("Не получен список пользователей в комнате или в нем нет не-администраторов");
        return;
    }

    let callback = function () {
        let output = document.getElementById("output_box");
        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
        const textNode = document.createTextNode("Пользователь изгнан");
        output.appendChild(textNode)
    };
    sendRequest("POST", "/api/room/kick", userLogin, callback, [7, 8, 9, 11]);

}

function setReady() {
    let callback = function () {
        getUsers()
    };
    sendRequest("POST", "/api/room/setReady", true, callback, [8]);
}

function unsetReady() {
    let callback = function () {
        getUsers()
    };
    sendRequest("POST", "/api/room/setReady", false, callback, [8]);
}