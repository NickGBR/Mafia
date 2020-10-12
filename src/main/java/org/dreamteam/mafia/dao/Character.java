package org.dreamteam.mafia.dao;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="character")

public class Character {

    @Column(name="game_id", nullable = false)
    private int gameId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="character_id", nullable = false)
    private Game game;

    @Column(name="role_id", nullable = false)
    private String role;

    @Column(name="status_id", nullable = false)
    private boolean status;
}
