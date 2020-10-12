package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "voting")

public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", unique = true, nullable = false)
    private int voteId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "day_number", nullable = false)
    private int numberOfDay;

    @Column(name = "votes_amount", nullable = false)
    private int votesAmount;

    @OneToMany(mappedBy = "voting")
    private List<Character> characterList;

}
