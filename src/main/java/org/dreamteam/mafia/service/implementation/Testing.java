package org.dreamteam.mafia.service.implementation;

import org.dreamteam.mafia.dao.User;
import org.dreamteam.mafia.dto.UserDTO;

import java.util.Optional;

public class Testing {
    public static void main(String[] args) {
        UserDTO u = new UserDTO();
        u.setLogin("Poly");

        UserServiceImpl usi = new UserServiceImpl();

        Optional<User> byLogin = usi.findByLogin(u);
        System.out.println(byLogin);
    }
}
