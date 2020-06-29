package com.zzp.ws2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * websocket的配置
 * <p>
 *  //TODO
 *  WebSocketConfig.java
 * </p>
 * @version v1.0.0
 * @author 佐斯特勒
 * @date 2020/6/29 21:01
 * @see  WebSocketConfig
 **/
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
