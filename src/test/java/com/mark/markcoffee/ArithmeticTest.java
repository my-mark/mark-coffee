package com.mark.markcoffee;


import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSON;
import com.mark.markcoffee.entity.Person;
import com.mark.markcoffee.util.DateTimeUtil;

import javax.swing.tree.TreeNode;
import java.rmi.dgc.Lease;
import java.util.*;

import static org.apache.commons.lang3.ArrayUtils.swap;


/**
 * 算法
 */
public class ArithmeticTest {

    private static int[] numbers = {26, 53, 7, 48, 57, 13, 48, 32, 60, 50};

    private static Person[] persons = {
            new Person("小王", "男", 29, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25"))),
            new Person("小田", "男", 36, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25"))),
            new Person("小海", "男", 46, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25"))),
            new Person("小赵", "男", 39, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25"))),
            new Person("小狗", "男", 26, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25"))),
            new Person("小何", "男", 29, DateTimeUtil.localDate2Date(DateTimeUtil.parseLocalDate("1992-03-25")))
    };

    public static void main(String[] args) throws InterruptedException {
        /**
         *  排序
         */
        /**希尔排序*/
        /*System.out.println("希尔排序:");
        Arrays.stream(shellSort(numbers)).forEach(num -> System.out.print(num + " "));
        System.out.println("");*/
        /**
         * 归并排序 (稳定的排序算法)
         */
        /*System.out.println("归并排序:");
        Arrays.stream(mergeSort(numbers)).forEach(num -> System.out.print(num + " "));
        System.out.println("");*/
        /**快速排序*/
        int[] nums = {6, 5, 3, 9, 8};
       /* System.out.println("快速排序(递归实现)");
        quickSort(nums, 0, nums.length - 1);*/
        /*System.out.println("快速排序(栈实现)");
        quickSortByStack(nums, 0, nums.length - 1);
        Arrays.stream(nums).forEach(num -> System.out.print(num + " "));*/
        /**桶排序*/
        /*System.out.println("桶排序:");
        bucketSort(numbers,10);*/
        /**
         * 计数排序 (稳定的排序算法)
         * 时间复杂度:【最佳情况：T(n) = O(n+k)  最差情况：T(n) = O(n+k)  平均情况：T(n) = O(n+k)】
         * 计数排序不是比较排序，排序的速度快于任何比较排序算法。由于用来计数的数组C的长度取决于待排序数组中数据的范围（等于待排序数组的最大值与最小值的差加上1），这使得计数排序对于数据范围很大的数组，需要大量时间和内存。
         * 虽然它可以将排序算法的时间复杂度降低到O(N)，但是有两个前提需要满足：一是需要排序的元素必须是整数，二是排序元素的取值要在一定范围内，并且比较集中。只有这两个条件都满足，才能最大程度发挥计数排序的优势。
         */
        /*System.out.println("计数排序:");
        Arrays.stream(countingSort(numbers)).forEach(num -> System.out.print(num + " "));
        /*System.out.println("计数排序——优化版:");
        Arrays.stream(countingSortBetter(numbers)).forEach(num -> System.out.print(num + " "));
         */
        /**基数排序*/
        /*System.out.println("基数排序:");
        Arrays.stream(radixSort(numbers,2)).forEach(num -> System.out.print(num + "\t"));*/
        /**=============================== 二 叉 树 算 法 ===============================*/
        Solution solution = new Solution();
        //示例二叉树
        /*TreeNode treeNode = new TreeNode(8);
        treeNode.left = new TreeNode(5);
        treeNode.right = new TreeNode(11);
        treeNode.left.left = new TreeNode(4);
        treeNode.left.right = new TreeNode(6);
        treeNode.right.left = new TreeNode(9);
        treeNode.right.right = new TreeNode(15);
        //判断是否为二叉搜索树(leetcode-98)
        System.out.println(solution.isValidBST(treeNode));*/

        /**
         * 应用场景:一个字段标识多类型
         */
        int num = 6;
        Integer pow1 = (int)Math.pow(2, 1);
        Integer pow2 = (int)Math.pow(2,2);
        Integer pow3 = (int)Math.pow(2,3);
        Integer pow4 = (int)Math.pow(2,4);
        System.out.println(num&pow1);
        System.out.println(num&pow2);
        System.out.println(num&pow3);
        System.out.println(num&pow4);

        /**
         * 散列函数
         */
        System.out.println(hash("mark"));



    }

    /**
     * 散列函数
     * @param key
     * @return
     */
    static int hash(Object key) {
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & (16 -1); //capicity表示散列表的大小
    }
    /**
     * ——————————————————————————————————————————————————————————————————————————————————————
     * =================================== 排 序 算 法 =======================================
     * ——————————————————————————————————————————————————————————————————————————————————————
     */
    /**
     * 希尔排序
     *
     * @param array
     */
    private static int[] shellSort(int[] array) {
        int n = array.length;
        int gap = 1;
        while (gap < n / 3) { //动态定义间隔序列
            gap = 3 * gap + 1;
        }
        while (gap >= 1) {
            for (int i = gap; i < n; i++) {
                for (int j = i; j >= gap && (array[j] < array[j - gap]); j -= gap) {
                    int temp = array[j];
                    array[j] = array[j - gap];
                    array[j - gap] = temp;
                }
            }
            gap /= 3;
        }
        return array;
    }

    /**
     * 归并排序
     *
     * @param array
     * @return
     */
    private static int[] mergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     *
     * @param left
     * @param right
     * @return
     */
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }

    /**
     * 快速排序——栈实现
     *
     * @param array
     * @return
     */
    private static void quickSort(int[] array, int p, int r) {
        if (p >= r) {
            return;
        }
        int q = partition(array, p, r);
        quickSort(array, p, q - 1);
        quickSort(array, q + 1, r);
    }

    /**
     * 快速排序——非递归
     *
     * @param array
     * @param left
     * @param right
     * @return
     */
    private static void quickSortByStack(int[] array, Integer left, Integer right) {
        Stack<Integer> stack = new Stack<>();
        stack.push(left);
        stack.push(right);//后入的right，所以要先拿right
        while (!stack.empty())//栈不为空
        {
            System.out.println(stack.toString());
            Integer r = stack.pop();
            Integer l = stack.pop();

            Integer index = partition(array, l, r);
            if ((index - 1) > l)//左子序列
            {
                stack.push(l);
                stack.push(index - 1);
            }
            if ((index + 1) < r)//右子序列
            {
                stack.push(index + 1);
                stack.push(r);
            }
        }
    }

    /**
     * 快速排序——分区排序函数
     *
     * @param array
     * @param start
     * @param end
     */
    private static int partition(int[] array, int start, int end) {
        int pivot = array[end];
        int i = start;
        for (int j = start; j < end; j++) {
            if (array[j] < pivot) {
                swap(array, i, j);
                i += 1;
            }
        }
        swap(array, i, end);
        return i;
    }


    /**
     * 桶排序
     * @param array
     * @param range 根据数据选择桶的划分数量（根据划分逻辑进行定义）
     */
    private static void bucketSort(int[] array, int range) {
        ArrayList buckets[] = new ArrayList[range];
        for (int num : array) {
            int index = (int)Math.floor(num / 10);
            if (buckets[index] == null) {
                buckets[index] = new ArrayList();
            }
            buckets[index].add(num);
        }
        //各个桶进行插入排序
        for (int i = 0; i < buckets.length; i++) {
            if (null != buckets[i]) {
                Collections.sort(buckets[i]);
            }
        }
        //输出排序后的数据
        for (int i = 0; i < buckets.length; i++) {
            if (null != buckets[i]) {
                for (Object num : buckets[i]) {
                    System.out.print(num + "\t");
                }
            }
        }
    }


    /**
     * 计数排序
     *
     * @param array
     * @return
     */
    private static int[] countingSort(int[] array) {
        int max = Arrays.stream(array).max().getAsInt();
        //申请计数数组
        int[] count = new int[max + 1];
        //进行计数
        for (int i = 0; i < array.length; i++) {
            count[array[i]]++;
        }
        //进行计数累加
        for (int i = 1; i < count.length; i++) {
            count[i] = count[i] + count[i - 1];
        }
        //倒序输出结果数组
        int[] result = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            int index = count[array[i]];
            result[index - 1] = array[i];
            count[array[i]]--;
        }
        return result;
    }


    /**
     * 计数排序——优化版
     *
     * @param array
     * @return
     */
    private static int[] countingSortBetter(int[] array) {
        int max = Arrays.stream(array).max().getAsInt();
        int min = Arrays.stream(array).min().getAsInt();
        //计数数组
        int[] count = new int[max - min + 1];
        for (int num : array) {
            count[num - min]++;
        }
        //结果数组
        int[] result = new int[array.length];
        /**
         * 倒序实现稳定排序
         */
        //定义结果数组的初始索引
        int index = array.length - 1;
        //处理数据
        for (int i = count.length - 1; i >= 0; i--) {
            while (count[i] > 0) {
                result[index--] = i + min;
                count[i]--;
            }
        }
        return result;
    }


    /**
     * 基数排序
     * @param array
     * @param distance  排序循环次数（数据位数）
     * @return
     */
    private static int[] radixSort(int[] array, int distance) {
        //用于存储每次基数排序的数组
        int[] temp = new int[array.length];
        //基数
        int radix = 10;
        //方便取余换算
        int divider = 1;
        //计数排序数组
        int[] count = new int[radix];

        for (int i = 0; i < distance; i++) {
            //初始化数组进行计数排序
            System.arraycopy(array, 0, temp, 0, array.length);
            Arrays.fill(count, 0);
            for (int j = 0; j < temp.length; j++) {
                int num = (temp[j]/divider) % radix;
                count[num]++;
            }
            //计数累加
            for (int j = 1; j < radix ; j++) {
                count[j] = count[j] + count[j - 1];
            }
            for (int j = 0; j < array.length; j++) {
                int num = (temp[j]/divider) % radix;
                count[num]--;
                array[count[num]] = temp[j];
            }
            divider *= radix;
        }
        return array;
    }







    /**
     * ——————————————————————————————————————————————————————————————————————————————————————
     * =================================== 淘 汰 策 略 算 法 =================================
     * ——————————————————————————————————————————————————————————————————————————————————————
     */

    /**
     * 手写LRU算法
     *
     * @param <K>
     * @param <V>
     */
    class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private final int CACHE_SIZE;

        /**
         * 传递进来最多能缓存多少数据
         *
         * @param cache_size 缓存大小
         */
        public LRUCache(int cache_size) {
            super((int) Math.ceil(cache_size / 0.75) + 1, 0.75f, true);
            CACHE_SIZE = cache_size;
        }

        /**
         * 当数据超过缓存大小限制清除最老数据
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > CACHE_SIZE;
        }
    }




    /**
     * ——————————————————————————————————————————————————————————————————————————————————————
     * =================================== 二 叉 树 算 法 =================================
     * ——————————————————————————————————————————————————————————————————————————————————————
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    public static class Solution {
        /**
         * 判断是否为二叉搜索树
         * @param root
         * @return
         */
        public boolean isValidBST(TreeNode root) {
            if (root == null || (root.left == null && root.right == null)) return true;
            return isValid(root,null,null);
        }

        public boolean isValid(TreeNode root, Integer min,Integer max) {
            if (root == null) return true;
            if (min != null && root.val <= min) return false;
            if (max != null && root.val >= max) return false;
            System.out.println(String.format("root.val = %d,min = %d,max = %d",root.val,min,max));
            return  isValid(root.left, min,root.val) &&
                    isValid(root.right, root.val,max);
        }
    }

}
