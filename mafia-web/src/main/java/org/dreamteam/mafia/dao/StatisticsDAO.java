package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "statistics")

public class StatisticsDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_id", unique = true, nullable = false)
    private Integer statisticsId;

    @Column(name = "games_total_as_mafia", nullable = false)
    private Integer gamesTotalAsMafia;

    @Column(name = "games_total_as_sheriff", nullable = false)
    private Integer gamesTotalAsSheriff;

    @Column(name = "games_total_as_citizen", nullable = false)
    private Integer gamesTotalAsCitizen;

    @Column(name = "games_won_as_mafia", nullable = false)
    private Integer gamesWonAsMafia;

    @Column(name = "games_won_as_sheriff", nullable = false)
    private Integer gamesWonAsSheriff;

    @Column(name = "games_won_as_citizen", nullable = false)
    private int gamesWonAsCitizen;

    @OneToOne(mappedBy = "statistics")
    private UserDAO user;

    public StatisticsDAO(UserDAO user, int gamesTotalAsMafia, int gamesTotalAsSheriff, int gamesTotalAsCitizen,
                         int gamesWonAsMafia, int gamesWonAsSheriff, int gamesWonAsCitizen) {
        this.user = user;
        this.gamesTotalAsMafia = gamesTotalAsMafia;
        this.gamesTotalAsSheriff = gamesTotalAsSheriff;
        this.gamesTotalAsCitizen = gamesTotalAsCitizen;
        this.gamesWonAsMafia = gamesWonAsMafia;
        this.gamesWonAsSheriff = gamesWonAsSheriff;
        this.gamesWonAsCitizen = gamesWonAsCitizen;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "user=" + user.getLogin() +
                ", gamesTotalAsMafia=" + gamesTotalAsMafia +
                ", gamesTotalAsSheriff=" + gamesTotalAsSheriff +
                ", gamesTotalAsCitizen=" + gamesTotalAsCitizen +
                ", gamesWonAsMafia=" + gamesWonAsMafia +
                ", gamesWonAsSheriff=" + gamesWonAsSheriff +
                ", gamesWonAsCitizen=" + gamesWonAsCitizen +
                '}';
    }
}
