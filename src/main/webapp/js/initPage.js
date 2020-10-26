let init = null;


let initialisedUserName = "";
let initialisedRoomID = "";
let initialisedUserRole = "";
let initialisedRoomName = "";
let initialisedIsAdmin = false;
let initialisedIsReady = false;
let pageName = getCurrentName();

function getCurrentName() {
    const path = window.location.pathname;
    return path.split("/").pop();
}


function onLoadInit() {
    startSpinner();
    doRedirect();
}

function doRedirect() {
    let callback = function (request) {
        const data = JSON.parse(request.responseText);
        console.log("do redirect = ");
        console.log(data);
        init = data;
        if (data["isLoggedIn"]) {
            initialisedUserName = data["name"];
            if (data["isInRoom"]) {
                initialisedRoomID = data["roomID"];
                initialisedRoomName = data["roomName"];
                initialisedIsAdmin = data["isAdmin"];
                initialisedIsReady = data["isReady"];
                if (data["isGameStarted"] && pageName !== "gameChat.html") {
                    window.location.href = "gameChat.html";
                } else if (data["isGameStarted"] && pageName === "gameChat.html") {
                    initialisedUserRole = data["role"];
                    doInitGameChat();
                } else if (pageName !== "roomChat.html") {
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
            }
        }
    };
    sendRequest("GET", "/api/init", "", callback, []);
}


function doInitGameChat() {
    connect();
    stopSpinner();
}

function doInitRoomList() {
    connect();
    setUserName();
}

function doInitRoom() {
    connect();
    setUserName();
}

function setUserName() {
    let userNameNode = document.getElementById("userName");
    const textNode = document.createTextNode(initialisedUserName);
    userNameNode.appendChild(textNode);
}