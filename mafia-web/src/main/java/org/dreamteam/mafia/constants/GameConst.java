package org.dreamteam.mafia.constants;

import javax.persistence.criteria.CriteriaBuilder;

public class GameConst {
    private static final Integer durationPhase = 1;
    public static final Integer DURATION = 1000;
    public static final Integer FULL_ROOM = -1;
    public static final Integer CIVILIAN_DISCUSS_PHASE_DURATION = durationPhase;
    public static final Integer CIVILIAN_VOTING_PHASE_DURATION = 12;
    public static final Integer MAFIA_DISCUSS_PHASE_DURATION = durationPhase;
    public static final Integer MAFIA_VOTING_PHASE_DURATION = 12;
    public static final Integer DON_PHASE_DURATION = durationPhase;
    public static final Integer SHERIFF_PHASE_DURATION = durationPhase;
}

