package org.dreamteam.mafia.model;

import java.util.Optional;

/**
 *  user - пустой Optional, если пользователь не был найден
 *      или объект, описывающий  пользователя.
 *  identificationMessage - текстовое описание результата идентификации
 */
public class IdentificationResult {
//user
    Optional<User> user;
    String identificationMessage;

}
