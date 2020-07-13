package com.mark.markcoffee.thread;


import com.mark.markcoffee.entity.AlbumDto;
import com.mark.markcoffee.entity.ObjPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流器
 */
public class CurrentLimiter {

    /**
     * 对象池
     */
    private static List<AlbumDto> albumDtos = new ArrayList<>();
    private static final ObjPool<AlbumDto, AlbumDto> objPool = new ObjPool<>(10, AlbumDto.class);
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 流控(多线程环境下限制每秒请求接口次数)
     */
    //信号量初始化10
    final static int MAX_QPS = 10;
    final static Semaphore semaphore = new Semaphore(MAX_QPS, true);
    //用于统计次数
    final static AtomicInteger UNIT_CALL_COUNT = new AtomicInteger(0);
    final static AtomicInteger TOTAL_TASK_COUNT = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        //流控
        liukong();
        //对象池
//        objectPool();
//        System.out.println(albumDtos.get(0) == albumDtos.get(1));
    }


    /**
     * 实现对象池
     */
    private static void objectPool() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        // 通过对象池获取 t，之后执行
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                AlbumDto exec = null;
                try {
                    System.out.println("ready");
                    countDownLatch.await();
                    System.out.println("go");
                    exec = objPool.exec(t -> {
                        System.out.println("当前线程id:" + Thread.currentThread().getId() + ",当前获取到的对象：" + t.hashCode());
                        return t;
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                albumDtos.add(exec);
            });
        }
        countDownLatch.countDown();
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }


    /**
     * 流控程序
     */
    private static void liukong() throws InterruptedException {
        ScheduledExecutorService poolSchedule = Executors.newScheduledThreadPool(1);
        poolSchedule.scheduleAtFixedRate(() -> {
            semaphore.drainPermits();//重置许可为0
            semaphore.release(MAX_QPS);
            System.out.println("*****UNIT_CALL_COUNT=" + UNIT_CALL_COUNT.get());
        }, 0, 1000, TimeUnit.MILLISECONDS);

        long startTime = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            if (i == 5) {
                Thread.sleep(2000); //模拟请求量不饱和的周期
            }
            pool.execute(CurrentLimiter::run);
        }
        pool.shutdown();
        //5秒超时,返回线程池关闭状态
        while (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("线程池还在执行……");
            Thread.sleep(1000);
        }
        //等待任务处理完毕
        while (semaphore.availablePermits() != 10) ;
        poolSchedule.shutdown();
        poolSchedule.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(String.format("Done: TOTAL_TASK_COUNT=%d TOTAL_TIME=%d", TOTAL_TASK_COUNT.get(), (System.currentTimeMillis() - startTime)));
    }

    /**
     * 流控程序调用方法
     */
    private static void remoteCall() {
        UNIT_CALL_COUNT.incrementAndGet();
        TOTAL_TASK_COUNT.incrementAndGet();
    }

    private static void run() {
        semaphore.acquireUninterruptibly(1);
        remoteCall();
    }

}
