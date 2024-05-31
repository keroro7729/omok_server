package com.test.omok;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRoom {

    private List<WebSocketSession> players = new ArrayList<>();

    public void addPlayer(WebSocketSession session){
        players.add(session);
    }

    public void selectColor(){
        Collections.shuffle(players);
    }

    public WebSocketSession getBlackPlayer(){
        return players.get(0);
    }

    public WebSocketSession getWhitePlayer(){
        return players.get(1);
    }

    public WebSocketSession opponent(WebSocketSession session){
        int idx = players.indexOf(session);
        if(idx == 0) return players.get(1);
        else if(idx == 1) return players.get(0);
        else return null;
    }
}
