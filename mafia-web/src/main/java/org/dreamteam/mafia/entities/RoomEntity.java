package org.dreamteam.mafia.entities;

import lombok.Getter;
import lombok.Setter;
import org.dreamteam.mafia.model.GamePhaseEnum;
import org.dreamteam.mafia.model.GameStatusEnum;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Объект, соответствующий строке таблицы комнат в БД
 */
@Getter
@Setter
@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", unique = true, nullable = false)
    private Long roomId;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "room_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_users_amount", nullable = false)
    private Integer maxUsersAmount;

    @Column(name = "game_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatusEnum gameStatus;

    @Column(name = "day_number")
    private Integer dayNumber;

    @Column(name = "game_phase")
    @Enumerated(EnumType.STRING)
    private GamePhaseEnum gamePhase;

    @Column(name = "mafia")
    private Integer mafia;

    @Column(name = "sheriff")
    private Boolean sheriff;

    @Column(name = "don")
    private Boolean don;

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! REFRESH
    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<UserEntity> userList;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<MessageEntity> messageList;

    public RoomEntity() {
        this.dayNumber = 0;
        this.gamePhase = GamePhaseEnum.CIVILIANS_DISCUSS_PHASE;
        this.gameStatus = GameStatusEnum.NOT_STARTED;
        this.messageList = new HashSet<>();
        this.userList = new HashSet<>();
    }

    public RoomEntity(
            String passwordHash, String roomName, Integer maxUsersAmount, GameStatusEnum gameStatus) {
        this.passwordHash = passwordHash;
        this.name = roomName;
        this.maxUsersAmount = maxUsersAmount;
        this.gameStatus = gameStatus;
    }

    public void addUser(UserEntity user) {
        this.userList.add(user);
    }

    public void removeUser(UserEntity user) {
        this.userList.remove(user);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoomDAO{");
        sb.append("roomId=").append(roomId);
        sb.append(", passwordHash='").append(passwordHash).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", maxUsersAmount=").append(maxUsersAmount);
        sb.append(", gameStatus=").append(gameStatus);
        sb.append(", dayNumber=").append(dayNumber);
        sb.append(", gamePhase=").append(gamePhase);
        sb.append(", mafia=").append(mafia);
        sb.append(", sheriff=").append(sheriff);
        sb.append(", don=").append(don);
        sb.append(", users={ ");
        boolean first = true;
        for (UserEntity user : userList) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(user.getLogin());
            if (user.getIsAdmin()) {
                sb.append("(admin)");
            }
        }
        sb.append("} }");
        return sb.toString();
    }
}
