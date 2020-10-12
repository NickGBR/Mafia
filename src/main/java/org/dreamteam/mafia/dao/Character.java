package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "character")

public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "character_id", unique = true, nullable = false)
    private Game game;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToOne(mappedBy = "character")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_id")
    private Voting voting;
}
