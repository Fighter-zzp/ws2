package com.zzp.ws2.service;

import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
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
@Service
@ServerEndpoint("/room/{userName}")
public class WebSocketServerService {

}
