package com.mark.markcoffee.thread;

public class CyclicBarrier {

    //取中间索引
    public static int pivotIndex(int[] nums) {
        int sum = 0, leftsum = 0;
        for (int x: nums) sum += x;
        for (int i = 0; i < nums.length; ++i) {
            if (leftsum == sum - leftsum - nums[i]) return i;
            leftsum += nums[i];
        }
        return -1;
    }

    public static boolean isPalindrome(String s) {
        char[] cs = s.toCharArray();
        int index = 0;
        for(int i=0;i<cs.length;i++){
            if(('0'<=cs[i] && cs[i]<='9') || ('a'<=cs[i] && cs[i]<='z')){
                cs[index++]=cs[i];
            }else if('A'<=cs[i] && cs[i]<='Z'){
                cs[index++]=(char)(cs[i]-('A'-'a'));
            }
        }
        int l=0,r=index-1;
        while(l<r){
            if(cs[l++] != cs[r--]){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
//        int[] nums = {1,7,3,6,5,6};
//        System.out.println(pivotIndex(nums));
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));

       /* // 订单队列
        Vector<P> pos;
        // 派送单队列
        Vector<D> dos;
        // 执行回调的线程池
        Executor executor =
                Executors.newFixedThreadPool(1);
        final CyclicBarrier barrier =
                new CyclicBarrier(2, () -> {
                    executor.execute(() -> check());
                });

        void check () {
            P p = pos.remove(0);
            D d = dos.remove(0);
            // 执行对账操作
            diff = check(p, d);
            // 差异写入差异库
            save(diff);
        }

        void checkAll () {
            // 循环查询订单库
            Thread T1 = new Thread(() -> {
                while (存在未对账订单) {
                    // 查询订单库
                    pos.add(getPOrders());
                    // 等待
                    barrier.await();
                }
            });
            T1.start();
            // 循环查询运单库
            Thread T2 = new Thread(() -> {
                while (存在未对账订单) {
                    // 查询运单库
                    dos.add(getDOrders());
                    // 等待
                    barrier.await();
                }
            });
            T2.start();
        }*/
    }
}
