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

function getUserName() {
    const token = readCookie("token");
    console.log("Current token: " + token);
    let request = new XMLHttpRequest();
    request.open("GET", "/api/user/getCurrentName", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer " + token);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                let output = document.getElementById("output_box");
                output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
                const textNode = document.createTextNode("Имя пользователя: " + request.responseText);
                output.appendChild(textNode)
            } else if (request.status === 401) {
                console.log("Non authorised!");
            } else if (request.status === 500) {
                const data = JSON.parse(request.responseText);
                switch (parseInt(data["result"])) {
                    case 1: {
                        console.log("Error: Internal logic error");
                        break;
                    }
                    case 2: {
                        console.log("Error: Database error");
                        break;
                    }
                }
            } else {
                console.log("Error: " + request.status);
            }
        }
    };
    request.send()
}

function createRoom() {
    const token = readCookie("token");
    console.log("Current token: " + token);
    let request = new XMLHttpRequest();
    request.open("POST", "/api/room/create", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer " + token);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                let output = document.getElementById("output_box");
                output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
                const textNode = document.createTextNode("Комната создана");
                output.appendChild(textNode)
            } else if (request.status === 400) {
                const data = JSON.parse(request.responseText);
                switch (parseInt(data["result"])) {
                    case 5: {
                        let output = document.getElementById("output_box");
                        output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
                        const textNode = document.createTextNode("Комната уже существует. Создание невозможно");
                        output.appendChild(textNode)
                        break;
                    }
                    default: {
                        console.log("Error: internal logic failure. Registration was successful, but login  of the same user failed.")
                    }
                }
            } else if (request.status === 401) {
                console.log("Non authorised!");
            } else if (request.status === 500) {
                const data = JSON.parse(request.responseText);
                switch (parseInt(data["result"])) {
                    case 1: {
                        console.log("Error: Internal logic error");
                        break;
                    }
                    case 2: {
                        console.log("Error: Database error");
                        break;
                    }
                }
            } else {
                console.log("Error: " + request.status);
            }
        }
    };
    const jsonData = {
        'name': 'testRoom',
        'description': 'This is test room'
    };
    request.send(JSON.stringify(jsonData))
}

function getRooms() {
    const token = readCookie("token");
    console.log("Current token: " + token);
    let request = new XMLHttpRequest();
    request.open("GET", "/api/room/getInitialList", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer " + token);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                let output = document.getElementById("output_box");
                output.textContent = ''; // Убираем все элементы потомки, заменяя их на пустую строку
                const data = JSON.parse(request.responseText);
                data.forEach(function (item, index) {
                    const textNode = document.createTextNode("Имя комнаты: " + item.name);
                    const node = document.createElement("LI");      // Создаем элемент списка <li>
                    node.appendChild(textNode)
                    output.appendChild(node)
                });

            } else if (request.status === 401) {
                console.log("Non authorised!");
            } else if (request.status === 500) {
                const data = JSON.parse(request.responseText);
                switch (parseInt(data["result"])) {
                    case 1: {
                        console.log("Error: Internal logic error");
                        break;
                    }
                    case 2: {
                        console.log("Error: Database error");
                        break;
                    }
                }
            } else {
                console.log("Error: " + request.status);
            }
        }
    };
    request.send()
}