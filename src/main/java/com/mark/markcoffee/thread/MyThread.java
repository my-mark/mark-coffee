package com.mark.markcoffee.thread;

import com.ylkz.util.DateTimeUtil;
import lombok.var;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 多线程唤醒
 */
public class MyThread {

    static Object obj = new Object();
    /**用于并发环境下存取线程*/
    static Map<Object, Thread> gos = new ConcurrentHashMap<>();
    /**用于并发环境下保存当前线程本地变量*/
    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();


    public static void main(String[] args) {
        //面试题   自增运算
        /*int i = 1;
        i = i++;
        int j = i++;
        int k = i + ++i * i++;
        System.out.println(i);
        System.out.println(j);
        System.out.println(k);*/

//        LockSupportTest();
//        waitAndNotifal();
       /* StringBuffer qwe = new StringBuffer();
        System.out.println(qwe.length());
        System.out.println(qwe==null ? "ok" : "no");
        qwe.append(123);
        System.out.println(qwe.length());*/
        /*Vector vobj = new Vector();
        Vector<Long> vobjList = new Vector<>();

        vobj.add(1);
        vobj.add(2);
        vobj.set(0, "a");


        for (Object o : vobj) {
            System.out.println(o);
        }*/


        //自动清除线程本地变量
        try (var ctx = new UserContext("Bob")) {
            // 可任意调用UserContext.currentUser():
            String currentUser = UserContext.currentUser();
            System.out.println(currentUser);
        }   // 在此自动调用UserContext.close()方法释放ThreadLocal关联对象
        System.out.println(UserContext.currentUser());
    }

    static class InterruptTest implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getState());
            System.out.println("唤醒-解除中断标记");
        }
    }

    /**==============================waitAndNotifal==============================*/
    static void waitAndNotifal() {
        new Thread(new WaitThread()).start();
        new Thread(new NotifyThread()).start();
    }

    static class WaitThread implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                System.out.println("start wait!");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end wait!");
            }
        }
    }

    static class NotifyThread implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                System.out.println("start notify!");
                obj.notify();
                System.out.println("end notify");
            }
        }
    }

    /**==============================LockSupport==============================*/
    static void LockSupportTest() {
        /**审核线程1*/
        MyThreadOne corp = new MyThreadOne();
//        corp.setName("123");
        corp.start();
        /**审核线程2*/
        /*MyThreadOne corp1 = new MyThreadOne();
        corp1.setName("");
        corp1.start();*/
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程是否存活："+corp.isAlive());
        System.out.println("线程状态："+corp.getState());
//        corp.interrupt();
        /**回调线程*/
        MyThreadTwo callback = new MyThreadTwo();
        callback.setName("callback");
        callback.start();
    }

    static class MyThreadOne extends Thread {

        @Override
        public void run() {
            System.out.println(" Enter Thread running.....");
            /**放入线程管理器*/
            gos.put(123, Thread.currentThread());
            /**线程阻塞*/
            System.out.println(Thread.currentThread().getName()+"线程阻塞。。。");
            System.out.println("before:"+Thread.currentThread().isInterrupted());
            System.out.println(DateTimeUtil.formatLocalDateTime(DateTimeUtil.now()));
            LockSupport.parkUntil(System.currentTimeMillis()+(TimeUnit.MILLISECONDS.toSeconds(10000)));//10秒超时
            System.out.println(DateTimeUtil.formatLocalDateTime(DateTimeUtil.now()));
            System.out.println("after:"+Thread.currentThread().isInterrupted());
//            LockSupport.parkNanos(1000000000*10);//10秒超时（纳秒）
//            LockSupport.parkUntil(1000*10);//10秒超时（毫秒）
            System.out.println(Thread.currentThread().getName()+"线程完成释放！");
        }

    }

    static class MyThreadTwo extends Thread {

        @Override
        public void run() {
            System.out.println(" 线程释放中.....");
            Thread thread = gos.remove(123);
//            Thread thread1 = gos.remove("corp1");
            thread.setName("qwe");
            LockSupport.unpark(thread);
        }

    }

    /**
     * 当一个线程需要用到同一个值，我们可以开辟一个线程独立存储空间ThreadLocal，通过实现AutoCloseable可以实现自动清除内存
     */
    static class UserContext implements AutoCloseable {

        static final ThreadLocal<String> ctx = new ThreadLocal<>();

        public UserContext(String user) {
            ctx.set(user);
        }

        public static String currentUser() {
            return ctx.get();
        }

        @Override
        public void close() {
            ctx.remove();
        }
    }
}