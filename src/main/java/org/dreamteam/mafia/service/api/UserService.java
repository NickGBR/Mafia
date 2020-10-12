package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.dao.User;

import java.util.Optional;

/**
 * Интерфейс сервиса, записсывающего и предоставляющего информацию о пользователе данной сессии
 */
public interface UserService {

    /**
     * Регистрирует нового пользователя в приложении
     * @param userDTO - информация о пользователе, полученная из интерфейса
     * @return - вновь зарегестрированного пользователя
     * @throws UserRegistrationException - если при регистрации возникли проблемы
     */
    User registerNewUser(UserDTO userDTO) throws UserRegistrationException;

    /**
     * Возвращает пользователя, авторизованного в данной сессии.
     * Сама авторизация выполняется до передачи управления в контроллеры за счет интерсепторов Spring Security
     * @return - авторизованного в настоящий момент пользователя или null, если пользователь еще не авторизован,
     * например, при вызове этого метода из контроллера, отвечающего за регистрацию пользователя
     */
    Optional<User> getCurrentUser(User currentUser);

    Optional<User> findByLogin(UserDTO userDTO);
}
