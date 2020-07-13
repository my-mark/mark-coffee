package com.mark.markcoffee.thread;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentContainerTest {

    @BeforeEach
    void setUp() {
        System.out.println("测试开始");
    }

    @AfterEach
    void tearDown() {
        System.out.println("测试结束");
    }

    @Test
    void bingfa() {
        ConcurrentContainer concurrentContainer = new ConcurrentContainer();
        try {
            concurrentContainer.init();
            concurrentContainer.bingfa(10000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}