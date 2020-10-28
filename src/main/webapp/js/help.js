let helpOpen = false;

function switchHelp() {
    if (!helpOpen) {
        setupModal("modal-help");
    } else {
        hideModal();
    }
}