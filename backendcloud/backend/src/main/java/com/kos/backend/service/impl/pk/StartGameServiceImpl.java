package com.kos.backend.service.impl.pk;

import com.kos.backend.consumer.WebSocketServer;
import com.kos.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}
