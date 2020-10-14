package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.User;
import org.dreamteam.mafia.dto.UserDTO;
import org.dreamteam.mafia.exceptions.UserRegistrationException;
import org.dreamteam.mafia.repository.api.UserRepository;
import org.dreamteam.mafia.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.dreamteam.mafia.Application.emf;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private static final EntityManager em = emf.createEntityManager();


    @Override
    public User registerNewUser(UserDTO userDTO) throws UserRegistrationException {
/*        if (userDTO != null && userRepository.
            userRepository.save(userDTO);*/
        return null;
    }

    @Override
    public Optional<User> getCurrentUser(User currentUser) {

        em.getTransaction().begin();
        User user = em.find(User.class, currentUser.getUserId());
        em.getTransaction().commit();

        return Optional.of(user);
    }

    @Override
    public Optional<User> findUserByLogin(UserDTO userDTO) {

        em.getTransaction().begin();

        try {
            User user = (User) em.createQuery("select user from User user where user.login = ?1")
                    .setParameter(1, userDTO.getLogin()).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            System.out.println("User with login \'" + userDTO.getLogin() + "\' doesn't exist. Please try again in 2805.");
        }

        em.getTransaction().commit();

        return Optional.empty();
    }

}
