package com.mark.markcoffee.thread;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentContainer {
    //请求阻塞队列
    private static LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<>();
    //统计总请求数
    private static AtomicInteger count = new AtomicInteger(0);
    //统计实际调用数据库请求数
    private static AtomicInteger dbCount = new AtomicInteger(0);

    /**
     * 请求内部类
     */
    @Data
    class Request {
        private String code;    //唯一标识
        private CompletableFuture<Map<String, Object>> future;   //返回结果
    }

    /**
     * 初始化定时任务（定时处理队列中的请求）
     * 10毫秒（根据实际业务场景设置）
     */
    @PostConstruct
    public void init() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (queue.size() == 0) {
                return;
            }
            ArrayList<Request> requests = new ArrayList<>();
            for (int i = 0; i < queue.size(); i++) {
                requests.add(queue.poll());
            }
            System.out.println(String.format("合并请求——本地共合并%d个请求", requests.size()));
            List<String> codes = new ArrayList<>();
            for (Request request : requests) {
                codes.add(request.getCode());
            }
            //调用批量查询接口
            List<Map<String, Object>> responseMap = batchRequest(codes);
            //处理数据
            Map<String, Map<String, Object>> resultMap = new HashMap<>();
            for (Map<String, Object> response : responseMap) {
                String code = response.get("code").toString();
                resultMap.put(code, response);
            }
            //返回给对应请求
            for (Request request : requests) {
                Map<String, Object> stringObjectMap = resultMap.get(request.getCode());
                request.future.complete(stringObjectMap);
            }


        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void bingfa(int questCount) throws IOException, InterruptedException {
        //模拟高并发请求查询接口
        CountDownLatch order = new CountDownLatch(1);
        CountDownLatch answer = new CountDownLatch(questCount);
        for (int i = 0; i < questCount; i++) {
            final String code = "code-" + (i + 1);
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("准备...");
                    order.await();
                    System.out.println("开始查询code"+code);
                    Map<String, Object> query = query(code);
                    System.out.println("返回结果："+ JSON.toJSONString(query));
                    answer.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            thread.setName("thread-" + i);
            thread.start();
        }
        System.out.println("开始并发调用");
        order.countDown();
        System.out.println("并发调用结束");
        answer.await();
        System.out.println(String.format("共查询了：%d条数据\n实际请求数据库次数：%d", count.get(),dbCount.get()));
        System.in.read();
    }


    /**
     * 模拟请求数据库——批量查询数据
     * 应对高并发场景过多请求造成的服务器压力问题
     */
    public List<Map<String, Object>> batchRequest(List<String> codes) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (String code : codes) {
            Map<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("code", code);
            objectObjectHashMap.put("msg", "data:" + code);
            results.add(objectObjectHashMap);
        }
        count.addAndGet(results.size());
        dbCount.incrementAndGet();
        return results;
    }

    /**
     * 请求接口调用
     */
    public Map<String, Object> query(String code) throws ExecutionException, InterruptedException {
        Request request = new Request();
        request.setCode(code);
        request.setFuture(new CompletableFuture<>());
        queue.add(request);
        return request.getFuture().get();
    }
}