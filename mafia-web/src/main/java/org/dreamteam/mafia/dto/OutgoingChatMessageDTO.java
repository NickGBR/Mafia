package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.MessageDAO;

@NoArgsConstructor
@Getter
@Setter
public class OutgoingChatMessageDTO {

    String text;
    String from;

    public OutgoingChatMessageDTO(MessageDAO dao) {
        this.from = dao.getUser().getLogin();
        this.text = dao.getText();
    }
}
