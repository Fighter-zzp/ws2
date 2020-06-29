package com.zzp.ws2;

import com.alibaba.fastjson.JSONObject;
import com.zzp.ws2.domain.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Ws2ApplicationTests {
    @Test
    void test1(){
        var msg = "{\n" +
                "  \"type\": 0,\n" +
                "  \"fromUser\": xxc,\n" +
                "  \"toUser\": xxp,\n" +
                "  \"msg\": test\n" +
                "}";
        var jsonObject = new JSONObject();
        var msg1 = jsonObject.getObject(msg, Message.class);
        System.out.println(msg1);
    }
}
