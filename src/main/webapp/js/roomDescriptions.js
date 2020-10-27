function generateDescription(data) {
    let description = "Игра на " + data["maxUserAmount"] + " игроков. Из них " + data["mafiaAmount"];
    if (data["mafiaAmount"] > 1) {
        description = description + " играют за Мафию."
    } else {
        description = description + " играет за Мафию."
    }
    if (data["hasDon"] && data["hasSheriff"]) {
        description = description + " В игре участвуют и Дон, и Шериф";
    } else if (data["hasDon"] && !data["hasSheriff"]) {
        description = description + " В игре участвует  Дон, но не Шериф";
    } else if (!data["hasDon"] && data["hasSheriff"]) {
        description = description + " В игре нет Дона,  но есть Шериф";
    } else {
        description = description + " В игре нет ни Дона, ни Шерифа";
    }
    return description;
}

function addDescriptionTooltip(node, data) {
    let tooltipNode = document.createElement("SPAN");
    tooltipNode.classList.add("tooltip");
    node.classList.add("tooltip-container");
    tooltipNode.innerText = generateDescription(data);
    node.appendChild(tooltipNode);
}

function removeDescriptionTooltip(node) {
    let tooltipNode = node.querySelector('.tooltip');
    node.removeChild(tooltipNode);
    node.classList.remove("tooltip-container");
}

