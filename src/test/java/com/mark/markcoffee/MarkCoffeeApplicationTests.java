package com.mark.markcoffee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkCoffeeApplicationTests {


    @Test
    public void contextLoads() {
        BigDecimal a = new BigDecimal(0);
        BigDecimal b = new BigDecimal(1);
        System.out.println(a.subtract(b));
    }


    @Test
    public void threadQuestion() throws InterruptedException {
        BigDecimal zero = new BigDecimal(0.01);
        System.out.println(zero);
    }

    private void corpUserExchange(CorpUser corpUser) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CorpUser corpUser = new CorpUser();
                    corpUser.setId(1L);
                    corpUser.setBalance(new BigDecimal(100));
                    corpUser.depositBalance(new BigDecimal(1));
                }
            }).start();
        }
    }
}
