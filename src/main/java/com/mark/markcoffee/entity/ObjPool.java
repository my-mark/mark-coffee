package com.mark.markcoffee.entity;

import org.springframework.cglib.core.internal.Function;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**对象池*/
public class ObjPool<T,R> {

        final List<T> pool;
        // 用信号量实现限流器
        final Semaphore sem;
        // 构造函数
        public ObjPool(int size, Class<T> tClass) {
            pool = new Vector<T>(){};
            for(int i=0; i<size; i++){
                try {
                    pool.add(tClass.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            sem = new Semaphore(size);
        }
        // 利用对象池的对象，调用 func
        public R exec(Function<T, R> func) throws InterruptedException {
            T t = null;
            sem.acquire();
            try {
                t = pool.remove(0);
                return func.apply(t);
            } finally {
//                pool.add(t);
                sem.release();
            }
        }

}
