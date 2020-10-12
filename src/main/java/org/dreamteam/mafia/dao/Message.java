package org.dreamteam.mafia.dao;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="message")

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id", nullable = false)
    private int messageId;

    @Column(name="game_id", nullable = false)
    private int gameId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(name="text", nullable = false)
    private String text;

    @Column(name="type", nullable = false)
    private String type;

}
