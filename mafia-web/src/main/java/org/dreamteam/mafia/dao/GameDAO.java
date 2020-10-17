package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.GamePhaseEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "phase", nullable = false)
    @Enumerated(EnumType.STRING)
    private GamePhaseEnum phase;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatusEnum status;

    @Column(name = "day_number", nullable = false)
    private Integer numberOfDay;

    @OneToOne(mappedBy = "game")
    private RoomDAO room;

    @OneToMany(mappedBy = "game")
    private List<VotingDAO> votingList;

    @OneToMany(mappedBy = "game")
    private List<CharacterDAO> characterList;

    @OneToMany(mappedBy = "game")
    private List<MessageDAO> messageList;

    public GameDAO(GamePhaseEnum phase, GameStatusEnum status, Integer numberOfDay, RoomDAO room, List<VotingDAO> votingList, List<CharacterDAO> characterList, List<MessageDAO> messageList) {
        this.phase = phase;
        this.status = status;
        this.numberOfDay = numberOfDay;
        this.room = room;
        this.votingList = votingList;
        this.characterList = characterList;
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + gameId +
                ", status=" + status +
                ", room=" + room +
                '}';
    }
}
