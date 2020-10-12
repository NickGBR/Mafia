package org.dreamteam.mafia.dao;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="room")

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id", nullable = false)
    private int roomId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="users_amount", nullable = false)
    private int usersAmount;

    @Column(name="password_hash")
    private long passwordHash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Game game;
}
