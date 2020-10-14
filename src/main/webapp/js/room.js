function getRooms() {

    const jsonData = {
        'token': 42
    };

    console.log(jsonData);

    let request = new XMLHttpRequest();
    request.open("GET", "/api/room/getAvailable", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                console.log(request.responseText);
            } else if (request.status === 401) {
                console.log("Non authorised!");
            }
        }
    };
    request.onerror = function () {

    }
    request.send()
}