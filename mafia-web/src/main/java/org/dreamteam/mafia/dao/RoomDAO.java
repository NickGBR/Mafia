package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private GameDAO game;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "max_users_amount", nullable = false)
    private Integer maxUsersAmount;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Set<UserDAO> userList;

    public RoomDAO(GameDAO game, String name, String description,
                   String passwordHash, Integer maxUsersAmount, Set<UserDAO> userList) {
        this.game = game;
        this.name = name;
        this.description = description;
        this.passwordHash = passwordHash;
        this.maxUsersAmount = maxUsersAmount;
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "RoomDAO{" +
                "roomId=" + roomId +
                ", gameId=" + game.getGameId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", maxUsersAmount=" + maxUsersAmount +
                ", userList=" + userList +
                '}';
    }
}
