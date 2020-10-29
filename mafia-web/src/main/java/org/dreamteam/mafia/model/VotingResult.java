package org.dreamteam.mafia.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Результат голосования против игрока
 */
@Getter
@Setter
@NoArgsConstructor
public class VotingResult {
    String login;
    Integer result;
}
