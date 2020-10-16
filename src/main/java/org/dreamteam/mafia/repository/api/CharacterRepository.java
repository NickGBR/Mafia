package org.dreamteam.mafia.repository.api;

import org.dreamteam.mafia.dao.CharacterDAO;
import org.dreamteam.mafia.dao.converter.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CharacterRepository extends CrudRepository<CharacterDAO, Long> {

    List<CharacterDAO> findByRole(RoleEnum roleEnum);

}
