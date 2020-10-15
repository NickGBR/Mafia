package org.dreamteam.mafia.service.implementation;

import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl  {

   /* @PersistenceContext
    private EntityManager em;

    public Optional<User> findUserByLogin(LoginDTO loginDTO) {

        em.getTransaction().begin();

        try {
            User user = (User) em.createQuery("select user from User user where user.login = ?1")
                    .setParameter(1, loginDTO.getLogin()).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            System.out.println("User with login \'" + loginDTO.getLogin() + "\' doesn't exist. Please try again in 3875.");
        }

        em.getTransaction().commit();

        return Optional.empty();
    }*/

}
