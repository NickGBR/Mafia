package org.dreamteam.mafia.dao;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="statistics")

public class Statistics {

    @Id
    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="games_total_as_mafia", nullable = false)
    private int gamesTotalAsMafia;

    @Column(name="games_total_as_sheriff", nullable = false)
    private int gamesTotalAsSheriff;

    @Column(name="games_total_as_citizen", nullable = false)
    private int gamesTotalAsCitizen;

    @Column(name="games_won_as_mafia", nullable = false)
    private int gamesWonAsMafia;

    @Column(name="games_won_as_sheriff", nullable = false)
    private int gamesWonAsSheriff;

    @Column(name="games_won_as_citizen", nullable = false)
    private int gamesWonAsCitizen;
}
