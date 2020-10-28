package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.MessageDAO;
import org.dreamteam.mafia.dao.enums.DestinationEnum;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDTO {

    String text;
    String from;
    DestinationEnum destination;

    public ChatMessageDTO(MessageDAO dao) {
        if (dao.getUser() != null) {
            this.from = dao.getUser().getLogin();
        } else {
            this.from = "";
        }
        this.text = dao.getText();
        this.destination = dao.getDestination();
    }
}
