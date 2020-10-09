package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.util.OptionalWithMessage;

public interface AuthorisationService {
    /**
     * Регистрирует нового пользователя в приложении
     * @param newLogin - логин регистрируемого пользователя
     * @param newPassword - пароль регистрируемого пользователя
     * @return - true, если регистрация прошла успешно, false, если регистрация не удалась
     */
    boolean registerNewUser(String newLogin, String newPassword);

    /**
     * Пытается найти пользователя по заданным учетным данным.
     * @param login - логин искомого пользователя
     * @param password - пароль искомого пользователя
     * @return - результат идентификации: пользователя или пустой Optional с сообщением об ошибке
     */
    OptionalWithMessage<User> identifyUser(String login, String password);
}
