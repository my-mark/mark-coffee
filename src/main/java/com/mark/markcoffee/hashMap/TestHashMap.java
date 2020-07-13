package com.mark.markcoffee.hashMap;

public class TestHashMap {

    public static void main(String[] args) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("刘在石", "刘大神");
        hashMap.put("HAHA", "哈哈");
        System.out.println(hashMap.get("刘在石"));
    }
}
