function sendUserInformation() {

    let callback = function (request) {
        const data = request.responseText
        console.log("Successful registration and login. New token: " + data);
        document.cookie = "token" + "=" + data + ";path=/";
        // Добавленно Никитой.
        sessionStorage.setItem("token", data);
        window.open("roomList.html", "_self");
    };
    const jsonData = {
        'login': document.getElementById("login_input").value,
        'password': document.getElementById("password_input").value,
        'passwordConfirmation': document.getElementById("repeated_password_input").value
    };
    sendRequest("POST", "/api/user/register", JSON.stringify(jsonData), callback, [1, 2]);

}