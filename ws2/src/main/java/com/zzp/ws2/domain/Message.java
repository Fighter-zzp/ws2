package com.zzp.ws2.domain;

import lombok.Data;

/**
 * 消息实体类
 * <p>
 *  //TODO
 *  Message.java
 * </p>
 * @version v1.0.0
 * @author 佐斯特勒
 * @date 2020/6/29 21:04
 * @see  Message
 **/
@Data
public class Message {
    /**
     * 聊天类型：0群聊，1私聊
     */
    private int type;

    /**
     * 发送方
     */
    private String fromUser;

    /**
     * 接收方
     */
    private String toUser;

    /**
     * 消息
     */
    private String msg;
}
