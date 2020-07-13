package com.mark.markcoffee.hashMap;

public class HashMap<K, V> implements Map<K, V> {

    private Entry<K, V>[] table = null;

    private int size = 0;

    public HashMap() {
        table = new Entry[16];
    }

    @Override
    public V put(K k, V v) {
        int index = hash(k);
        Entry<K, V> entry = table[index];
        if (null == entry) {
            table[index] = new Entry<>(k, v, index, null);
            size++;
        } else {
            //Java 1.7 头插法
            table[index] = new Entry<>(k, v, index, entry);
        }
        return table[index].getValue();
    }

    @Override
    public V get(K k) {
        if (size == 0) {
            return null;
        }
        int index = hash(k);
        Entry<K, V> entry = getEntry(k, index);
        return null == entry ? null : entry.getValue();
    }


    @Override
    public int size() {
        return size;
    }

    //通过hash算法得出下标
    private int hash(K k) {
        int index = k.hashCode() % 15;
        return Math.abs(index);
    }

    //获取entry(如果为链表就循环获取进行比对)
    private Entry getEntry(K k, int index) {
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.hash == index && (k == entry.getKey() || entry.getKey().equals(k))) {
                return entry;
            }
        }
        return null;
    }

    class Entry<K, V> implements Map.Entry<K, V> {

        K k;
        V v;
        int hash;
        Entry<K, V> next;

        public Entry(K k, V v, int hash, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }
}
