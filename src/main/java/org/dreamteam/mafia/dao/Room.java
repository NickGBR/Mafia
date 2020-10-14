package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "rooms")

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", unique = true, nullable = false)
    private int roomId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password_hash")
    private long passwordHash;

    @Column(name = "users_amount", nullable = false)
    private int usersAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Game game;

    @OneToMany(mappedBy = "room")
    private List<User> userList;

}
