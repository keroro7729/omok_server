package com.test.omok;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MatchQue {

    private List<WebSocketSession> que = new LinkedList<>();
    private WebSocketHandler webSocketHandler;
    private final String header = "match-response:";

    public MatchQue(WebSocketHandler webSocketHandler){
        this.webSocketHandler = webSocketHandler;
    }

    public void startQueing(WebSocketSession session){
        webSocketHandler.send(session, header+"wait");
        que.add(session);
        if(que.size() >= 2){
            WebSocketSession player1 = que.get(0);
            WebSocketSession player2 = que.get(1);
            que.remove(0); que.remove(0);

            boolean p1 = player1.isOpen(), p2 = player2.isOpen();
            if(p1 && p2){
                webSocketHandler.startGame(player1, player2);
            }
            else{
                if(p1) que.add(player1);
                if(p2) que.add(player2);
            }
        }
    }

    public void cancelQueing(WebSocketSession session){
        que.remove(session);
    }
}
