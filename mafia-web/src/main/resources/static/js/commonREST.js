/**
 * Отправляет запрос,
 * если запрос отправлен в комнате -
 * комната отпределяется автоматически на стороне сервера.
 * @param method
 * @param url
 * @param data
 * @param callback
 * @param allowedErrors
 */
function sendRequest(method, url, data, callback, allowedErrors) {
    const token = sessionStorage.getItem('token');
    console.log("Current token: " + token);
    let request = new XMLHttpRequest();
    request.open(method, url, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Bearer " + token);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (processResponse(request, allowedErrors)) {
                if(callback!==null) {
                    callback(request);
                }
            }
        }
    };
    request.send(data);
}

function processResponse(request, allowedCodes) {
    if (isOkay(request)) {
        return true;
    }
    if (isUnauthorised(request)) {
        window.location.href = "login.html";
        return false;
    }
    const errorMessage = processErrorResponse(request, allowedCodes);
    showModalMessage("Ошибка", errorMessage);
    return false;


}

function isOkay(request) {
    return request.status === 200
}

function isUnauthorised(request) {
    return request.status === 401
}

function processErrorResponse(request, allowedCodes) {
    if (request.status === 400) {
        const data = JSON.parse(request.responseText);
        const code = parseInt(data["result"]);
        if (code === -1) {
            return "Ошибка логики клиента. Сервер не смог распознать отправленный запрос. Обратитесь к разработчику."
        }
        if (!allowedCodes.includes(code)) {
            return "Ошибка внутренней логики. Сервер вернул код ошибки, недопустимый для данной операции. Обратитесь к разработчику.";
        }
        return processErrorCode(code);
    } else if (request.status === 500) {
        const data = JSON.parse(request.responseText);
        switch (parseInt(data["result"])) {
            case 1: {
                return "Ошибка внутренней логики. Сервер не смог обработать запрос. Обратитесь к разработчику.";
            }
            case 2: {
                return "Ошибка внутренней логики. База данных не смогла обработать запрос. Обратитесь к разработчику.";
            }
        }
    }
    return "Неизвестная ошибка. Сервер вернул код " + request.status;
}

function processErrorCode(code) {
    switch (code) {
        case 1: {
            return "Пароль и его подтверждение не совпадают"
        }
        case 2: {
            return "Такое имя пользователя уже используется"
        }
        case 3: {
            return "Такого пользователя не существует"
        }
        case 4: {
            return "Введен неверный пароль"
        }
        case 5: {
            return "Данное действие недопустимо, когда вы уже находитесь в комнате"
        }
        case 6: {
            return "Данное действие недопустимо в текущую игровую фазу"
        }
        case 7: {
            return "Данное действие недопустимо, так как целовой пользователь не находится в вашей комнате"
        }
        case 8: {
            return "Данное действие недопустимо, когда вы не находитесь в комнате"
        }
        case 9: {
            return "Только администратор комнаты может выполнять данное действие"
        }
        case 10: {
            return "Комната не существует"
        }
        case 11: {
            return "Данное действие недопустимо для комнаты, игра в которой уже началась"
        }
        case 12: {
            return "В комнате нет свободных мест"
        }
        case 13: {
            return "Задано недопустимое имя: пустое, либо же превышающее максимальную длину"
        }
        case 14: {
            return "Переданы недопустимые параметры комнаты"
        }
        case 15: {
            return "Данное действие недопустимо, когда ваш персонаж мертв"
        }
        case 16: {
            return "Данное действие недопустимо, пока игра в комнате не началась"
        }
        case 17: {
            return "Вы уже отдали свой голос"
        }
        default: {
            return "Ошибка внутренней логики. Сервер вернул код ошибки, неизвестный клиенту. Обратитесь к разработчику.";
        }
    }
}