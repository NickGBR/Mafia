let spinner;
let initialisedUserName = "";
let initialisedRoomID = "";
let initialisedRoomName = "";
let initialisedIsAdmin = false;
let initialisedIsReady = false;
let pageName = getCurrentName();

function getCurrentName() {
    const path = window.location.pathname;
    return path.split("/").pop();
}


function onLoadInit() {
    spinner = new Spin.Spinner().spin();
    document.getElementById("body").appendChild(spinner.el);
    doRedirect();
}

function doRedirect() {
    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        if (data["isLoggedIn"]) {
            initialisedUserName = data["name"];
            if (data["isInRoom"]) {
                initialisedRoomID = data["roomID"];
                initialisedRoomName = data["roomName"];
                initialisedIsAdmin = data["isAdmin"];
                initialisedIsReady = data["isReady"];
                if (pageName !== "roomChat.html") {
                    window.location.href = "roomChat.html";
                } else {
                    doInitRoom();
                }
            } else {
                if (pageName !== "roomList.html") {
                    window.location.href = "roomList.html";
                } else {
                    doInitRoomList();
                }
            }
        } else {
            if (pageName !== "login.html" && name !== "registration.html") {
                window.location.href = "login.html";
            } else {
                doInitLogin();
            }
        }
    };
    sendRequest("GET", "/api/init", "", callback, []);
}


function doInitLogin() {
    spinner.stop();
}

function doInitRoomList() {
    connect();
    setUserName();
    spinner.stop();
}

function doInitRoom() {
    connect();
    setUserName();
    spinner.stop();
}

function setUserName() {
    let userNameNode = document.getElementById("userName");
    const textNode = document.createTextNode(initialisedUserName);
    userNameNode.appendChild(textNode);
}