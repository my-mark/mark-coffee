package com.mark.markcoffee;


import org.assertj.core.data.MapEntry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Java——CAS
 */
public class ReactiveSteamTest {

    /**
     * Java对CAS中ABA问题的解决方案
     * AtomicStampedReference 本质是有一个int 值作为版本号，每次更改前先取到这个int值的版本号
     * 等到修改的时候，比较当前版本号与当前线程持有的版本号是否一致，
     * 如果一致，则进行修改，并将版本号+1（当然加多少或减多少都是可以自己定义的）
     * 在zookeeper中保持数据的一致性也是用的这种方式
     */
    private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<>(1, 0);

    /**compareAndSet(当前值,变更值,当前标记(版本号),变更标记(版本号))*/

    public static void main(String[] args){
        /*Thread main = new Thread(() -> {
            System.out.println("操作线程" + Thread.currentThread() +",初始值 a = " + atomicStampedRef.getReference());
            int stamp = atomicStampedRef.getStamp(); //获取当前标识别
            try {
                Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCASSuccess = atomicStampedRef.compareAndSet(1,2,stamp,stamp +1);  //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
            System.out.println("操作线程" + Thread.currentThread() +",CAS操作结果: " + isCASSuccess);
        },"主操作线程");

        Thread other = new Thread(() -> {
            Thread.yield(); // 确保thread-main 优先执行
            boolean incrementSuccess = atomicStampedRef.compareAndSet(1, 2, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
            System.out.println("操作线程" + Thread.currentThread() +",【increment】 ,值 = "+ atomicStampedRef.getReference() + ",CAS操作结果: " + incrementSuccess);
            boolean decrementSuccess = atomicStampedRef.compareAndSet(2, 1, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
            System.out.println("操作线程" + Thread.currentThread() +",【decrement】 ,值 = "+ atomicStampedRef.getReference() + ",CAS操作结果: " + decrementSuccess);
        },"干扰线程");

        main.start();
        other.start();*/
        LinkedHashMap<Object, Object> objectObjectLinkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < 105; i++) {
            objectObjectLinkedHashMap.put(i, i);
        }
        System.out.println(objectObjectLinkedHashMap.size());
    }



}
