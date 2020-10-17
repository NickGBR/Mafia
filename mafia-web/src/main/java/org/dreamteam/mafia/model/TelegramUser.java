package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Scope("prototype")
public class TelegramUser {
    boolean isStartButtonPressed = false;
    String chatId;
}
