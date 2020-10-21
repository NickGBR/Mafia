package org.dreamteam.mafia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomDisplayDTO {
    private Long id;
    private String name;
    private String description;
    private Integer currPlayers;
    private Integer maxPlayers;
    private Boolean privateRoom;
}
