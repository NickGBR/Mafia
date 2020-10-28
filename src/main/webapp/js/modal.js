function setupModal(modalID) {
    if ((typeof openChat !== 'undefined') && openChat) {
        switchChat();
    }

    const modal = document.getElementById(modalID);
    const overlay = document.getElementById("overlay-modal");
    const body = document.getElementById("body");
    const header = document.getElementById("header");
    modal.style.display = "block";
    overlay.style.display = "block";
    body.style.backgroundSize = "0";
    body.style.backgroundColor = "wheat";
    if (header !== null) {
        header.style.backgroundSize = "0";
        header.style.backgroundColor = "wheat";
    }
}

function hideModal() {
    const overlay = document.getElementById("overlay-modal");
    const body = document.getElementById("body");
    const header = document.getElementById("header");
    body.style.backgroundSize = "auto";
    if (header !== null) {
        header.style.backgroundSize = "auto";
    }
    let childrenArr = Array.prototype.slice.call(overlay.children);
    childrenArr.forEach((item) => {
        item.style.display = "none";
    });
    overlay.style.display = "none";
}

let confirmCallback = null;

function showModalMessage(title, text, callback = null) {
    hideModal();
    let textNode = document.getElementById("modal-message-text");
    textNode.innerText = text;
    let titleNode = document.getElementById("modal-message-title");
    titleNode.innerText = title;
    setupModal("modal-message");
    if (callback !== null) {
        confirmCallback = callback;
    }
}

function confirmModalMessage() {
    hideModal();
    if (confirmCallback !== null) {
        confirmCallback();
        confirmCallback = null;
    }
}

let spinner = null;
const opts = {
    lines: 8, // The number of lines to draw
    length: 0, // The length of each line
    width: 10, // The line thickness
    radius: 25, // The radius of the inner circle
    scale: 1, // Scales overall size of the spinner
    corners: 1, // Corner roundness (0..1)
    speed: 1.2, // Rounds per second
    rotate: 51, // The rotation offset
    animation: 'spinner-line-shrink', // The CSS animation name for the lines
    direction: 1, // 1: clockwise, -1: counterclockwise
    color: '#000000', // CSS color or array of colors
    fadeColor: 'transparent', // CSS color or array of colors
    top: '49%', // Top position relative to parent
    left: '50%', // Left position relative to parent
    shadow: '0 0 1px transparent', // Box-shadow for the lines
    zIndex: 2000000000, // The z-index (defaults to 2e9)
    className: 'spinner', // The CSS class to assign to the spinner
    position: 'absolute', // Element positioning
};


function startSpinner() {
    setupModal("modal-spinner")
    if (spinner === null) {
        spinner = new Spin.Spinner(opts);
    }
    spinner.spin();
    document.getElementById("modal-spinner").appendChild(spinner.el);
}

function stopSpinner() {
    spinner.stop();
    hideModal();
}

