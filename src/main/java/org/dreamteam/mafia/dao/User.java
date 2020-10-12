package org.dreamteam.mafia.dao;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")})
@NamedQueries({
        @NamedQuery(name = "get_users", query = "")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", unique = true, nullable = false)
    private int userId;

    @Column(name="login", nullable = false, length = 100)
    private String login;

    @Column(name="password_hash", nullable = false)
    private long passwordHash;

    @Column(name="room_id")
    private int roomId;

}
