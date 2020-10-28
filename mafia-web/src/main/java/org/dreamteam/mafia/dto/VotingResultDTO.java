package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VotingResultDTO {
    String login;
    Integer result;
}
