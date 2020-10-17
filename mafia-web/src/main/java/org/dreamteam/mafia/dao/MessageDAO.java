package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")

public class MessageDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", unique = true, nullable = false)
    private Integer messageId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private GameDAO game;

    @Column(name = "addressee", nullable = false)
    private Integer addresseeId;

    @Column(name = "text", nullable = false)
    private String text;

    public MessageDAO(UserDAO user, GameDAO game, Integer addresseeId, String text) {
        this.user = user;
        this.game = game;
        this.addresseeId = addresseeId;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + messageId +
                ", gameId=" + game.getGameId() +
                ", from=" + user.getLogin() +
                ", to userId=" + addresseeId +
                ", text='" + text + '\'' +
                '}';
    }
}
