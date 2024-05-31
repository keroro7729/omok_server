package com.test.omok;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.ByteBuffer;
import java.util.*;

@Component
@EnableWebSocket
public class WebSocketHandler extends TextWebSocketHandler implements WebSocketConfigurer {

    private final MatchQue matchQue = new MatchQue(this);
    private final Map<Integer, GameRoom> games = new HashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(this, "/omok") // websocket-endpoint ws://ip:port/omok
                .setAllowedOrigins("*");
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String[] payload = message.getPayload().split(":");
        switch(payload[0]){
            case "match-request": handleMatchRequest(session, payload); break;
            case "make-game":
                int gameKey = makeGameRoom();
                joinGameRoom(gameKey, session);
                break;
            case "join-game":
                joinGameRoom(Integer.parseInt(payload[1]), session);
                break;
            case "game-message": handleGameMessage(session, payload); break;

            default:
                session.sendMessage(new TextMessage("Unknown-payload"));
                session.close();
        }
    }

    private void handleMatchRequest(WebSocketSession session, String[] payload){
        String header = "match-response:";
        switch(payload[1]){
            case "start":
                matchQue.startQueing(session);
                break;
            case "cancel":
                matchQue.cancelQueing(session);
                break;
            case "pong":
                //matchQue.pong(session);
                break;
        }
    }

    private void handleGameMessage(WebSocketSession session, String[] payload) throws Exception {
        String header = "game-message:";
        int gameKey = Integer.parseInt(payload[1]);
        GameRoom game = games.get(gameKey);
        WebSocketSession opponent = game.opponent(session);
        switch(payload[2]){
            case "put": send(opponent, header+"opponent-put:"+payload[3]); break;
            case "resign": send(opponent, header+"opponent-resign"); break;
            case "draw-request": send(opponent, header+"opponent-draw-request"); break;
            case "draw-response": send(opponent, header+"opponent-draw-response:"+payload[3]); break;
            case "rematch":
                if(payload[3].equals("yes")){
                    game.selectColor();
                    send(game.getBlackPlayer(), header+"rematch-start:"+gameKey+" "+Const.BLACK);
                    send(game.getWhitePlayer(), header+"rematch-start:"+gameKey+" "+Const.WHITE);
                }
                else if(payload[3].equals("no")){
                    send(opponent, header+"opponent-leave");
                    session.close();
                }
        }
    }

    public void send(WebSocketSession session, String message){
        try{
            session.sendMessage(new TextMessage(message));
        }catch(Exception e){

        }
    }

    public void startGame(WebSocketSession player1, WebSocketSession player2){
        int gameKey = makeGameRoom();
        GameRoom game = games.get(gameKey);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.selectColor();
        send(game.getBlackPlayer(), "match-response:start:"+gameKey+" "+Const.BLACK);
        send(game.getWhitePlayer(), "match-response:start:"+gameKey+" "+Const.WHITE);
    }

    public int makeGameRoom(){
        int gameKey = 1000;
        while(games.containsKey(gameKey)) gameKey = (int)(Math.random()*Integer.MAX_VALUE);
        games.put(gameKey, new GameRoom());
        return gameKey;
    }

    public boolean joinGameRoom(int gameKey, WebSocketSession session){
        GameRoom game = games.get(gameKey);
        if(game == null) return false;
        game.addPlayer(session);
        return true;
    }
}



/*
    // client connection check ping

    private static final long PING_INTERVAL = 30000;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        TimerTask pingTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new PingMessage(ByteBuffer.wrap("ping".getBytes())));
                    } else {
                        this.cancel();
                    }
                } catch (Exception e) {
                    this.cancel();
                    try {
                        session.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(pingTask, PING_INTERVAL, PING_INTERVAL);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        //
    }
 */