package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "votings")

public class VotingDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", unique = true, nullable = false)
    private int voteId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @Column(name = "day_number", nullable = false)
    private int numberOfDay;

    @Column(name = "votes_amount", nullable = false)
    private int votesAmount;

    @OneToMany(mappedBy = "voting")
    private List<CharacterDAO> characterList;

    public VotingDAO(GameDAO game, int numberOfDay, int votesAmount, List<CharacterDAO> characterList) {
        this.game = game;
        this.numberOfDay = numberOfDay;
        this.votesAmount = votesAmount;
        this.characterList = characterList;
    }

  /*  @Override
    public String toString() {
        return "Voting{" +
                "voteId=" + voteId +
               // ", game=" + game +
                ", numberOfDay=" + numberOfDay +
                ", votesAmount=" + votesAmount +
                ", characterList=" + characterList +
                '}';
    }*/
}
