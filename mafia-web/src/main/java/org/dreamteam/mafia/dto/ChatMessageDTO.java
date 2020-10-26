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
        this.from = dao.getUser().getLogin();
        this.text = dao.getText();
        this.destination = dao.getDestination();
    }
}
