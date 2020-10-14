let token = "";

function attemptLogin() {
    const jsonData = {
        'login': "my_login",
        'password': "some_password"
    };
    let request = new XMLHttpRequest();
    request.open("POST", "/api/user/login", true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                const data = JSON.parse(request.responseText);
                console.log(data);
                switch (parseInt(data["result"])) {
                    case 0: {
                        console.log("Success. New token: " + data["token"]);
                        token = data["token"];
                        break;
                    }
                    case 3: {
                        console.log("Error: no such user");
                        break;
                    }
                    case 4: {
                        console.log("Error: wrong password");
                        break;
                    }
                }
            } else {
                console.log("Error: " + request.status);
            }
        }
    };
    request.send(JSON.stringify(jsonData));
}


function getRooms() {
    console.log("Current token: " + token);
    let request = new XMLHttpRequest();
    request.open("GET", "/api/room/getAvailable", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer " + token);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                console.log(request.responseText);
            } else if (request.status === 401) {
                console.log("Non authorised!");
            }
        }
    };
    request.send()
}