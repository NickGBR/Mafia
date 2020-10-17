package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.RoleEnum;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;

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
    private Integer characterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO userId;

    @JoinColumn(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterStatusEnum status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vote_id")
    private VotingDAO voting;

    public CharacterDAO(GameDAO game, UserDAO userId, RoleEnum role, CharacterStatusEnum status, VotingDAO voting) {
        this.game = game;
        this.userId = userId;
        this.role = role;
        this.status = status;
        this.voting = voting;
    }

    @Override
    public String toString() {
        return "Character{" +
  //              "game=" + game +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
