function tryToLogin() {
    const jsonData = {
        'login': document.getElementById("login_input").value,
        'password': document.getElementById("password_input").value
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
                        document.cookie = "token" + "=" + data["token"] + ";path=/";
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