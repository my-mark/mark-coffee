package com.mark.markcoffee.thread;


import com.alibaba.fastjson.JSON;
import com.mark.markcoffee.entity.AlbumDto;
import com.ylkz.entity.vo.ResultMessage;
import com.ylkz.enums.ResponseCode;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 多线程——Future——应用
 */
public class Future {

    private static AtomicInteger count = new AtomicInteger(0);
    //创建工作队列
    private static final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(100);

    //创建线程池
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 5, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {

//            completableFutureTest();
        futureTaskTest();
//        completableFutureExceptionTest();
//        shaoshuipaocha();
//        testBatch();
    }

    public static void futureTaskTest() {
        /**
         * 包装任务
         */
        FutureTask<ResultMessage> resultMessageFutureTask1 = new FutureTask<>(new Xunwen1(10));
        FutureTask<ResultMessage> resultMessageFutureTask2 = new FutureTask<>(new Xunwen1(20));
        FutureTask<ResultMessage> resultMessageFutureTask3 = new FutureTask<>(new Xunwen1(30));
        AlbumDto albumDto = new AlbumDto();
        albumDto.setImgPath("123");
        FutureTask<ResultMessage> resultMessageFutureTask4 = new FutureTask<>(new Xunwen2(albumDto));


        try {
            /**
             * 提交任务执行
             */
            threadPoolExecutor.submit(resultMessageFutureTask1);
            threadPoolExecutor.submit(resultMessageFutureTask2);
            threadPoolExecutor.submit(resultMessageFutureTask3);

            java.util.concurrent.Future<AlbumDto> submit = threadPoolExecutor.submit(resultMessageFutureTask4, albumDto);

            /**
             * 获取返回结果
             */
            System.out.println(JSON.toJSONString(resultMessageFutureTask1.get()));
            System.out.println(JSON.toJSONString(resultMessageFutureTask2.get()));
            System.out.println(JSON.toJSONString(resultMessageFutureTask3.get()));
            System.out.println(JSON.toJSONString(submit.get()));
            //关闭线程
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(20, TimeUnit.SECONDS);
            System.out.println("线程池已关闭");
            System.out.println("任务已执行" + count.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class Xunwen1 implements Callable<ResultMessage> {

        private Integer money;

        public Xunwen1(Integer money) {
            this.money = money;
            System.out.println("报价" + money + "万元");
        }

        @Override
        public ResultMessage call() throws Exception {
            count.getAndIncrement();
            return new ResultMessage(ResponseCode.SUCCESS.getCode(), "报价" + money + "万元", money);
        }
    }

    static class Xunwen2 implements Callable<ResultMessage> {

        public Xunwen2(AlbumDto albumDto) {
            albumDto.setImgPath("456");
        }

        @Override
        public ResultMessage call() throws Exception {
            count.getAndIncrement();
            return new ResultMessage(ResponseCode.SUCCESS.getCode(), "操作成功", 10);
        }
    }


    /**
     * completableFuture
     */
    public static void completableFutureTest() {

        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(() -> {
                    int t = 0;
                    try {
                        t = new Random().nextInt();
                        t = 2000;
                        System.out.println(t);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return String.valueOf(t);
                });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    int t = 0;
                    try {
                        t = new Random().nextInt();
                        System.out.println(t);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return String.valueOf(t);
                });

        CompletableFuture<String> f3 =
                f1.applyToEither(f2, s -> s + "abc");

        System.out.println(f3.join());
    }


    /**
     * 异常捕获
     */
    public static void completableFutureExceptionTest() {
        CompletableFuture<Integer>
                f0 = CompletableFuture
                .supplyAsync(() -> 7 / 0)
                .thenApply(r -> r * 10)
                .exceptionally(e -> 0);
        System.out.println(f0.join());

    }


    /**
     * 烧水泡茶
     */
    public static void shaoshuipaocha() {

        //任务1：洗水壶->烧开水
        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("T1:洗水壶...");
                    sleep(1, TimeUnit.SECONDS);

                    System.out.println("T1:烧开水...");
                    sleep(15, TimeUnit.SECONDS);
                });
        //任务2：洗茶壶->洗茶杯->拿茶叶
        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("T2:洗茶壶...");
                    sleep(1, TimeUnit.SECONDS);

                    System.out.println("T2:洗茶杯...");
                    sleep(2, TimeUnit.SECONDS);

                    System.out.println("T2:拿茶叶...");
                    sleep(1, TimeUnit.SECONDS);
                    return "龙井";
                });
        //任务3：任务1和任务2完成后执行：泡茶
        CompletableFuture<String> f3 =
                f1.thenCombine(f2, (__, tf) -> {
                    System.out.println("T1:拿到茶叶:" + tf);
                    System.out.println("T1:泡茶...");
                    return "上茶:" + tf;
                });
        //等待任务3执行结果
        System.out.println(f3.join());
    }

    static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
        }
    }


    /**
     * ===============================================================================================================
     */
    public static void testBatch() {

        ArrayList<Object> demandDataDtoList = new ArrayList<>();
        demandDataDtoList.add(1);
        demandDataDtoList.add(2);
        demandDataDtoList.add(3);

        /**
         * 初始化线程池——同步企业数据
         */
        //创建工作队列
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(demandDataDtoList.size());
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 15, 5, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.AbortPolicy());
        /**
         * 初始化异步任务工具
         */
        BlockingQueue<java.util.concurrent.Future<ResultMessage>> bq = new LinkedBlockingQueue<>(demandDataDtoList.size());
        CompletionService<ResultMessage> cs = new ExecutorCompletionService<>(threadPoolExecutor, bq);
        //循环执行任务
        demandDataDtoList.forEach(demandDataDto -> cs.submit(() -> processData(demandDataDto)));
    }

    private static ResultMessage processData(Object object) {
        if(object.equals(2)) {
            int num = 1 / 0;
        }
        System.out.println("success");
        return new ResultMessage(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc());
    }

}