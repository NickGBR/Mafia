let spinner;

function getCurrentName() {
    const path = window.location.pathname;
    return path.split("/").pop();
}


function onLoadInit() {
    spinner = new Spin.Spinner().spin("modal");
    document.getElementById("body").appendChild(spinner.el);
    doRedirectToLogin();
}

function doRedirectToLogin() {
    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        if (data) {
            doRedirectToRoomList();
        } else {
            const name = getCurrentName();
            if (name !== "login.html" && name !== "registration.html") {
                window.location.href = "login.html";
            } else {
                doInitLogin();
            }
        }
    };
    sendRequest("GET", "/api/user/isLoggedIn", "", callback, []);
}

function doRedirectToRoomList() {
    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        if (data) {
            doRedirectToRoom();
        } else {
            const name = getCurrentName();
            if (name !== "roomList.html") {
                window.location.href = "roomList.html";
            } else {
                doInitRoomList();
            }
        }
    };
    sendRequest("GET", "/api/room/isInRoom", "", callback, []);
}

function doRedirectToRoom() {
    if (name !== "roomChat.html") {
        window.location.href = "roomChat.html";
    } else {
        doInitRoom();
    }
}

function doInitLogin() {
    spinner.stop();
}

function doInitRoomList() {
    spinner.stop();
}

function doInitRoom() {
    spinner.stop();
}