function tryToLogin() {

    let callback = function (request) {
        const data = request.responseText
        console.log("Success. New token: " + data);
        document.cookie = "token" + "=" + data + ";path=/";
        // Добавленно Никитой.
        sessionStorage.setItem("token", data);
        window.open("roomList.html", "_self");
    };
    const jsonData = {
        'login': document.getElementById("login_input").value,
        'password': document.getElementById("password_input").value
    };
    sendRequest("POST", "/api/user/login", JSON.stringify(jsonData), callback, [3, 4]);
}