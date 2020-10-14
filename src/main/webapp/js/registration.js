function sendUserInformation() {


    const jsonData = {
        'login': document.getElementById("login_input").value,
        'password': document.getElementById("password_input").value,
        'passwordConfirmation': document.getElementById("repeated_password_input").value
    };

    console.log(jsonData);

    let request = new XMLHttpRequest();
    request.open("POST", "/register", true);
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.send(JSON.stringify(jsonData));
}