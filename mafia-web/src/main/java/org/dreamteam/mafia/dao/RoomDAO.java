package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password_hash")
    private Long passwordHash;

    @Column(name = "users_amount", nullable = false)
    private Integer usersAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private GameDAO game;

   @OneToMany(mappedBy = "room")
   private List<UserDAO> userList;

    public RoomDAO(String name, Long passwordHash, Integer usersAmount, GameDAO game, List<UserDAO> userList) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.usersAmount = usersAmount;
        this.game = game;
     //   this.userList = userList;
    }

    @Override
    public String toString() {
        return "RoomDAO{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", usersAmount=" + usersAmount +
                ", gameId=" + game.getGameId() +
             //   ", userList=" + userList.size() +
                '}';
    }
}
