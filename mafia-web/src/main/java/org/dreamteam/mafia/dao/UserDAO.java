package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.CharacterEnum;
import org.dreamteam.mafia.dao.enums.CharacterStatusEnum;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * Объект, связанный с таблицей пользователей в БД
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")

public class UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "login", unique = true, nullable = false, length = 100)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users2rooms",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = {@JoinColumn(name = "room_id")}
    )
    @NotFound(action = NotFoundAction.IGNORE)
    private RoomDAO room;

    @Column(name = "is_ready")
    private Boolean isReady;

    @Column(name = "character")
    @Enumerated(EnumType.STRING)
    private CharacterEnum character;

    @Column(name = "character_status")
    @Enumerated(EnumType.STRING)
    private CharacterStatusEnum characterStatus;

    @Column(name = "votes_against")
    private Integer votesAgainst;

    public UserDAO(String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
        isReady = false;
        character = CharacterEnum.CITIZEN;
        characterStatus = CharacterStatusEnum.ALIVE;
        votesAgainst = 0;
        room = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDAO{");
        sb.append("login='").append(login).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
