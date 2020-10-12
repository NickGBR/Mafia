package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private int userId;

    @Column(name = "login", unique = true, nullable = false, length = 100)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private long passwordHash;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private Statistics statistics;

    @OneToMany(mappedBy = "user")
    private List<Message> messageList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "character_id")
    private Character character;

}
