package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.DestinationEnum;
import org.dreamteam.mafia.dao.enums.GameStatusEnum;

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
    private Long messageId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private RoomDAO room;

    @Column(name = "destination", nullable = false)
    @Enumerated(EnumType.STRING)
    private DestinationEnum destination;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDAO user;

    @Column(name = "text", nullable = false)
    private String text;



    public MessageDAO(RoomDAO room, UserDAO user, String text) {
        this.room = room;
        this.user = user;
        this.text = text;
    }

    @Override
    public String toString() {
        return "MessageDAO{" +
                "messageId=" + messageId +
                ", roomId=" + room.getRoomId() +
                ", user=" + user.getLogin() +
                ", text='" + text + '\'' +
                '}';
    }
}
