package com.mark.markcoffee.pubSub;

import com.mark.markcoffee.util.RedisUtil;
import com.ylkz.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "redisListen")
public class RedisListenController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "setExpireKey",method = RequestMethod.GET)
    public void setExpireKey(){
        System.out.println(DateTimeUtil.localDateTime2Date(DateTimeUtil.now()));
        redisUtil.set("testListen", "test", TimeUnit.SECONDS.toSeconds(10));
    }

}
