package com.mark.markcoffee.config;

import com.ylkz.util.DateTimeUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @description: 监听器
 * @method:
 * @author: Mark
 * @date: 11:39 2018/6/6
 */
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(DateTimeUtil.localDateTime2Date(DateTimeUtil.now()) + message.toString());
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        if(expiredKey.startsWith("orderNo:")){
            //如果是order:开头的key，进行处理
            System.out.println(expiredKey);
            String substring = expiredKey.substring(8); //去掉orderNo
            System.out.println(substring);
        }
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(),StandardCharsets.UTF_8);
        System.out.println(String.format("redis key 过期：pattern={},channel={},key={}",new String(pattern),channel,key));
    }


}
