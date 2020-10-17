package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.RoleEnum;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "characters")

public class CharacterDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id", unique = true, nullable = false)
    private Integer characterId;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterStatusEnum status;

    @JoinColumn(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vote_id")
    private VotingDAO voting;

    public CharacterDAO(UserDAO user, GameDAO game, CharacterStatusEnum status, RoleEnum role, VotingDAO voting) {
        this.user = user;
        this.game = game;
        this.status = status;
        this.role = role;
        this.voting = voting;
    }

    @Override
    public String toString() {
        return "Character{" +
                "characterId=" + characterId +
                ", user=" + user.getLogin() +
                ", gameId=" + game.getGameId() +
                ", status=" + status +
                ", role=" + role +
                ", voting=" + voting +
                '}';
    }
}
