package com.ztgeo.general.config.webscoket;

import com.ztgeo.general.component.penghao.PositionComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.extend.NumberDisplay;
import com.ztgeo.general.entity.extend.ServerEncoder;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/websocket/{username}",encoders ={ ServerEncoder.class})
//此注解相当于设置访问URL
public class WebSocket {
    private Session session;//webscoket session

    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(username, session);
//        sendMessage();
        System.out.println("【websocket消息】有新的连接，总数为:"+webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        System.out.println("【websocket消息】连接断开，总数为:"+webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息:"+message);
    }

    // 此为广播消息
    public void sendAllMessage(Object object) {
        for(WebSocket webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息:"+object);
            try {
                webSocket.session.getAsyncRemote().sendObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String username, Object object) {
        Session session = sessionPool.get(username);
        if (session != null) {
            try {
                session.getAsyncRemote().sendObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendMessage(String username, Object object) {
        Session session = sessionPool.get(username);
        if (session != null) {
            try {
                session.getBasicRemote().sendObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Object SendResultMsagge(String username,String id){
        NumberDisplay numberDisplay = new NumberDisplay();
        ApproveService approveService = SpringContextHolder.getBean("approveServiceImpl");
        numberDisplay.setAgency(approveService.findAgencyNumber(username).toString());
        numberDisplay.setHangUp(approveService.findHandUpFlowNumber(id,username).toString());
        numberDisplay.setParticipationIncompleteNumber(approveService.findParticipationIncompleteNumber(username).toString());
        return numberDisplay;
    }





}
