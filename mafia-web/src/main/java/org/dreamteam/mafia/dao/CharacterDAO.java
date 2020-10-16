package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.converter.RoleEnum;
import org.dreamteam.mafia.model.Role;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "characters")

public class CharacterDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id", unique = true, nullable = false)
    private int characterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO userId;

    // @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "status", nullable = false)
    private boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_id", nullable = false)
    private VotingDAO voting;

    @Override
    public String toString() {
        return "Character{#" + characterId +
                '}';
    }
}
