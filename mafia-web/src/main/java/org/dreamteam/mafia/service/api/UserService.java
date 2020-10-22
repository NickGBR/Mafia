package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.UserDAO;
import org.dreamteam.mafia.dto.LoginDTO;
import org.dreamteam.mafia.dto.RegistrationDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.security.SignedJsonWebToken;

import java.util.Optional;

/**
 * Интерфейс сервиса, записсывающего и предоставляющего информацию о пользователе данной сессии
 */
public interface UserService {

    /**
     * Регистрирует нового пользователя в приложении
     *
     * @param registrationDTO - информация о пользователе, полученная из интерфейса
     * @throws ClientErrorException - если при регистрации возникли проблемы:
     *                              пользователь уже существует
     *                              или пароль не совпадает с подтверждением
     */
    void registerNewUser(RegistrationDTO registrationDTO) throws ClientErrorException;

    /**
     * Вход существующего пользователя в приложение
     *
     * @param loginDTO - информация о пользователе, полученная из интерфейса
     * @return - JWS (подписанный JWT), соответствующий пользователю
     * @throws ClientErrorException - если при входе возникли проблемы:
     *                              пользователя не существует
     *                              или введен неверный пароль
     */
    SignedJsonWebToken loginUser(LoginDTO loginDTO) throws ClientErrorException;

    /**
     * Возвращает пользователя, авторизованного в данной сессии.
     * Сама авторизация выполняется до передачи управления в контроллеры за счет интерсепторов Spring Security
     *
     * @return - авторизованного в настоящий момент пользователя или null, если пользователь еще не авторизован,
     * например, при вызове этого метода из контроллера, отвечающего за регистрацию пользователя
     */
    Optional<User> getCurrentUser();

    /**
     * Возвращает БД-сущность пользователя, авторизованного в данной сессии.
     *
     * @return - БД-сущность авторизованного в настоящий момент пользователя или null, если пользователь еще не авторизован
     */
    Optional<UserDAO> getCurrentUserDAO();
}
