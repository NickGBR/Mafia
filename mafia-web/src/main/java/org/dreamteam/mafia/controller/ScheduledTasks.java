package org.dreamteam.mafia.controller;

import org.dreamteam.mafia.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

    @Component
    public class ScheduledTasks {
        int i = 0;
        Game game = new Game();
        @Autowired
        private SimpMessagingTemplate template;

//        @Scheduled(fixedRate = 10000)
//        public void sendStat() {
//            //System.out.println(i++);
//            //game.setNight(!game.isNight());
//            this.template.convertAndSend("/chat/game_stat", game);
//        }
    }

