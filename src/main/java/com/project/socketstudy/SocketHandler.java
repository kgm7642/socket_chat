package com.project.socketstudy;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
 
@Component
public class SocketHandler extends TextWebSocketHandler {
    
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
    
    @Override
//	소켓 연결시 동작하는 메소드(소캣 연결되면 세션을 매개변수로 받아온다.)
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	
        super.afterConnectionEstablished(session);
        System.out.println("세션의 아이디"+session.getId());
        System.out.println("세션 정보"+session);
        sessionMap.put(session.getId(), session);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료시 동작하는 메소드
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
        System.out.println("소캣 연결 종료");
    }
    
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송시 동작하는 메소드
        String msg = message.getPayload();
        System.out.println("메세지 확인 : " + msg);
        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                wss.sendMessage(new TextMessage(msg));
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
