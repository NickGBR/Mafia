package org.dreamteam.mafia.services.api;

import org.dreamteam.mafia.model.IdentificationResult;

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
     * @return - результат идентификации
     */
    IdentificationResult identifyUser(String login, String password);
}
