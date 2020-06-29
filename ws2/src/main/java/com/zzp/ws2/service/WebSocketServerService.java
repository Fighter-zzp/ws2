package com.zzp.ws2.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzp.ws2.domain.Message;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * webSocketServer服务
 * <p>
 *  //TODO
 *  WebSocketServerService.java
 * </p>
 * @version v1.0.0
 * @author 佐斯特勒
 * @date 2020/6/29 21:03
 * @see  WebSocketServerService
 **/
@EqualsAndHashCode
@Service
@ServerEndpoint("/room/{nickName}")
@Slf4j
public class WebSocketServerService {
    /**
     * 昵称
     */
    private String nickName;

    /**
     * socket会话
     */
    private Session session;

    /**
     * 存放websocket对象
     */
    private static CopyOnWriteArraySet<WebSocketServerService> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     * 用来记录sessionId和该session进行绑定
     */
    private static Map<String, Session> map = new ConcurrentHashMap<>();

    /**
     * 建立会话
     * @param session 会话
     * @param nickName 昵称
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("nickName")String nickName){
        var msg = new JSONObject();
       this.session = session;
       this.nickName = nickName;
        map.put(session.getId(), session);
        webSocketSet.add(this);
        log.info("{}加入连接，当前在线有{}人",nickName,webSocketSet.size());
        //消息类型，0-连接成功，1-用户消息
        msg.put("type",0);
        //在线人数
        msg.put("people",webSocketSet.size());
        //昵称
        msg.put("name",nickName);
        //频道号
        msg.put("aisle",session.getId());
        this.session.getAsyncRemote().sendText(msg.toJSONString());
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        log.info("{}退出聊天室，剩余{}人",nickName,webSocketSet.size());
    }

    @OnMessage
    public void OnMessage(Session session,@PathParam("nickName") String nickName,String msg){
        var jsonObject = new JSONObject();
        Message message;
        message = JSON.parseObject(msg,Message.class);
        if (message.getType() == 1){
            // 私聊
            message.setToUser(session.getId());
            var fromUser = map.get(message.getFromUser());
            var toUser = map.get(message.getToUser());
            var user = Optional.ofNullable(toUser);
            user.ifPresentOrElse(u->{
                var map = new ConcurrentHashMap<String, Object>(16);
                map.put("type",1);
                map.put("name",nickName);
                map.put("msg",message.getMsg());
                fromUser.getAsyncRemote().sendText(JSON.toJSONString(map));
                u.getAsyncRemote().sendText(JSON.toJSONString(map));
            },()->{
                fromUser.getAsyncRemote().sendText("系统消息：对方未在线");
            });
        }else {
            //群发消息
            broadcast(nickName + ": " + message.getMsg());
        }
    }

    /**
     * 发生错误时调用   
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误,用户：{}，发生错误",session.getId());
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message) {
        webSocketSet.forEach(item->item.session.getAsyncRemote().sendText(message));
    }
}
