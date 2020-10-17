package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "games")

public class GameDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id", unique = true, nullable = false)
    private Integer gameId;

    @OneToOne(mappedBy = "game")
    private RoomDAO room;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatusEnum status;

    @Column(name = "phase", nullable = false)
    @Enumerated(EnumType.STRING)
    private GamePhaseEnum phase;

    @Column(name = "day_number", nullable = false)
    private Integer numberOfDay;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<VotingDAO> votingList;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<CharacterDAO> characterList;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<MessageDAO> messageList;

    public GameDAO(RoomDAO room, GameStatusEnum status, GamePhaseEnum phase,
                   Integer numberOfDay, Set<VotingDAO> votingList,
                   Set<CharacterDAO> characterList, Set<MessageDAO> messageList) {
        this.room = room;
        this.status = status;
        this.phase = phase;
        this.numberOfDay = numberOfDay;
        this.votingList = votingList;
        this.characterList = characterList;
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", roomId=" + room.getRoomId() +
                ", status=" + status +
                ", phase=" + phase +
                ", numberOfDay=" + numberOfDay +
                ", votingList=" + votingList +
                ", characterList=" + characterList +
                ", messageList=" + messageList +
                '}';
    }
}
