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

function getRooms() {
    const token = readCookie("token");
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