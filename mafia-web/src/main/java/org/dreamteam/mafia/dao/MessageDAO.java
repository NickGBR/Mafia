package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.enums.DestinationEnum;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "messages2rooms",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")}
    )
    private RoomDAO room;

    @Column(name = "destination", nullable = false)
    @Enumerated(EnumType.STRING)
    private DestinationEnum destination;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "messages2users",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private UserDAO user;

    @Column(name = "text", nullable = false)
    private String text;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageDAO{");
        sb.append("messageId=").append(messageId);
        if (room != null) {
            sb.append(", room=").append(room);
        } else {
            sb.append(", common chat");
        }

        sb.append(", destination=").append(destination);
        if (user != null) {
            sb.append(", user=").append(user);
        } else {
            sb.append(", system message");
        }
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
