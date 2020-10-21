package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.UserDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data репозиторий для обеспечения CRUD доступа к пользователям
 */
@Repository
public interface UserRepository extends CrudRepository<UserDAO, Long> {

    /**
     * Находит всех пользователей с указанным логином
     *
     * @param login - логин
     * @return - список пользователей. Для текущей БД всегла 0 или 1 пользователь, т.к. логин уникален
     */
    Optional<UserDAO> findByLogin(String login);
}