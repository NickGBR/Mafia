package org.dreamteam.mafia.dao;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="game")

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_id", nullable = false)
    private int gameId;

    @Column(name="phase", nullable = false)
    private boolean phase;

    @Column(name="status", nullable = false)
    private boolean status;

    @Column(name="day_number", nullable = false)
    private int numberOfDay;

    @OneToOne(mappedBy = "game")
    private Room room;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gameId")
    private List<Voting> votingList;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game")
    private List<Character> characterList;

}
