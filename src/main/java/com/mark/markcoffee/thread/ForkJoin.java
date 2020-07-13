package com.mark.markcoffee.thread;

import lombok.var;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


/**
 * 分而治之
 */
public class ForkJoin {


    public static void main(String[] args) {
        String[] fc = {"hello world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};

        //创建ForkJoin线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        //1.创建任务
        MR mr = new MR(fc, 0, fc.length);
        //2.启动任务
        Map<String, Integer> invoke = forkJoinPool.invoke(mr);
        //3.输出结果
        invoke.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    static class MR extends RecursiveTask<Map<String, Integer>> {
        //data
        private String[] data;
        //起始结束下标
        private int start, end;

        public MR(String[] data, int start, int end) {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        //分而治之
        @Override
        protected Map<String, Integer> compute() {
            if (end - start == 1) {
                return calc(data[start]);
            } else {
                int mid = (end + start) / 2;
                MR mr1 = new MR(data, start, mid);
                mr1.fork();
                MR mr2 = new MR(data, mid, end);
                return merge(mr2.compute(), mr1.join());
            }
        }

        //合并结果
        private Map<String, Integer> merge(Map<String, Integer> map1, Map<String, Integer> map2) {
            Map<String, Integer> resultMap = new HashMap<>(16);
            resultMap.putAll(map1);
            //循环录入数据
            map2.forEach((k, v) -> {
                Integer value = resultMap.get(k);
                if (value != null) {
                    resultMap.put(k, value + v);
                } else {
                    resultMap.put(k, v);
                }
            });
            return resultMap;
        }

        //计算
        private Map<String, Integer> calc(String dataLine) {
            Map<String, Integer> resultMap = new HashMap<>(16);
            String[] words = dataLine.split("\\s");
            for (String word : words) {
                Integer value = resultMap.get(word);
                if (value != null) {
                    resultMap.put(word, ++value);
                } else {
                    resultMap.put(word, 1);
                }
            }
            return resultMap;
        }

    }

}
