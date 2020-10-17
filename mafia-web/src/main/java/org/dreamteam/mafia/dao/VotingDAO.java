package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "votings")

public class VotingDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", unique = true, nullable = false)
    private Integer voteId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @Column(name = "day_number", nullable = false)
    private Integer numberOfDay;

    @Column(name = "votes_amount", nullable = false)
    private Integer votesAmount;

    @OneToMany(mappedBy = "voting", fetch = FetchType.EAGER)
    private Set<CharacterDAO> characterList;

    public VotingDAO(GameDAO game, Integer numberOfDay, Integer votesAmount, Set<CharacterDAO> characterList) {
        this.game = game;
        this.numberOfDay = numberOfDay;
        this.votesAmount = votesAmount;
        this.characterList = characterList;
    }

    @Override
    public String toString() {
        return "Voting{" +
                "voteId=" + voteId +
                ", gameId=" + game.getGameId() +
                ", numberOfDay=" + numberOfDay +
                ", votesAmount=" + votesAmount +
                ", characterList=" + characterList.size() +
                '}';
    }
}
