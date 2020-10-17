package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Объект, связанный с таблицей пользователей в БД
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")

public class UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "login", unique = true, nullable = false, length = 100)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private RoomDAO room;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "statistics_id")
    private StatisticsDAO statistics;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<MessageDAO> messageList;

    public UserDAO(String login, String passwordHash, RoomDAO room, StatisticsDAO statistics, List<MessageDAO> messageList) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.room = room;
        this.statistics = statistics;
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", passwordHash='" + passwordHash + '\'' +
                ", login='" + login + '\'' +
                ", roomId=" + room.getRoomId() +
                ", statisticsId=" + statistics.getStatisticsId() +
                ", messageList=" + messageList +
                '}';
    }
}
