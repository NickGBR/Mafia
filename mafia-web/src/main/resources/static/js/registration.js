function sendUserInformation() {

    let login = document.getElementById("login_input").value;
    login = login.trim();
    if (login.length === 0) {
        showInputError("Ошибка: Введено пустое имя или же имя из одних пробелов");
        return;
    }
    if (login.length > 32) {
        showInputError("Ошибка: Введено имя длиннее 32 символов");
        return;
    }
    let password = document.getElementById("password_input").value;
    password = password.trim();
    let passwordConf = document.getElementById("repeated_password_input").value;
    passwordConf = passwordConf.trim();

    if (password.length === 0) {
        showInputError("Ошибка: не выбран пароль");
        return;
    }
    if (password !== passwordConf) {
        showInputError("Ошибка: указанные пароли не совпадают");
        return;
    }


    let callback = function (request) {
        const data = request.responseText
        console.log("Successful registration and login. New token: " + data);
        document.cookie = "token" + "=" + data + ";path=/";
        // Добавленно Никитой.
        sessionStorage.setItem("token", data);
        window.open("roomList.html", "_self");
    };
    const jsonData = {
        'login': login,
        'password': document.getElementById("password_input").value,
        'passwordConfirmation': document.getElementById("repeated_password_input").value
    };
    sendRequest("POST", "/api/user/register", JSON.stringify(jsonData), callback, [1, 2, 13]);
}

function showInputError(text) {
    const error = document.getElementById("input-error-message")
    error.innerText = text;
    error.style.display = "block";
}