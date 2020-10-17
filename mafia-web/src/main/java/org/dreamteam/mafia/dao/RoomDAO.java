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

    @Column(name = "description")
    private String description;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "max_users_amount", nullable = false)
    private Integer maxUsersAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private GameDAO game;

   @OneToMany(mappedBy = "room")
   private List<UserDAO> userList;

    public RoomDAO(String name, String passwordHash, Integer maxUsersAmount, GameDAO game) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.maxUsersAmount = maxUsersAmount;
        this.game = game;
     //   this.userList = userList;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", passwordHash=" + passwordHash +
                ", maxUsersAmount=" + maxUsersAmount +
              //  ", game=" + game +
               // ", userList=" + userList +
                '}';
    }
}
