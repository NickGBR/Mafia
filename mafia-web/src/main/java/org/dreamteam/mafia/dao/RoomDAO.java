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
@Table(name = "rooms")

public class RoomDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", unique = true, nullable = false)
    private Integer roomId;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id", nullable = false)
    private UserDAO admin;

    @Column(name = "room_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_users_amount", nullable = false)
    private Integer maxUsersAmount;

    @Column(name = "game_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatusEnum gameStatus;

    @Column(name = "day_number")
    private Integer dayNumber;

    @Column(name = "game_phase")
    @Enumerated(EnumType.STRING)
    private GamePhaseEnum gamePhase;

    @Column(name = "mafia")
    private Integer mafia;

    @Column(name = "sheriff")
    private Boolean sheriff;

    @Column(name = "don")
    private Boolean don;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<UserDAO> userList;

    @OneToMany(mappedBy = "messageId", fetch = FetchType.EAGER)
    private Set<MessageDAO> messageList;

    public RoomDAO(String passwordHash, UserDAO admin, String roomName, Integer maxUsersAmount, GameStatusEnum gameStatus) {
        this.passwordHash = passwordHash;
        this.admin = admin;
        this.name = roomName;
        this.maxUsersAmount = maxUsersAmount;
        this.gameStatus = gameStatus;
    }

    @Override
    public String toString() {
        return "RoomDAO{" +
                "roomId=" + roomId +
                ", passwordHash='" + passwordHash + '\'' +
                ", admin=" + admin.getLogin() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxUsersAmount=" + maxUsersAmount +
                ", gameStatus='" + gameStatus + '\'' +
                ", dayNumber=" + dayNumber +
                ", gamePhase='" + gamePhase + '\'' +
                ", mafia=" + mafia +
                ", sheriff=" + sheriff +
                ", don=" + don +
                ", userList=" + userList +
                ", messageList=" + messageList +
                '}';
    }
}
