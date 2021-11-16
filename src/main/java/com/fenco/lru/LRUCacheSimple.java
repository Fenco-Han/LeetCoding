package com.fenco.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: fenco
 * @Date: 2021/11/16 18:34
 * @ClassName: LRUCacheSimple
 * @Description: 一个更简单实用的LRUCache方案，使用LinkedHashMap即可实现。
 * LinkedHashMap提供了按照访问顺序排序的方案，内部也是使用HashMap+双向链表。
 * 只需要重写removeEldestEntry方法，当该方法返回true时，LinkedHashMap会删除最旧的结点。
 */
public class LRUCacheSimple {
    public static void main(String[] args) {
        // 定义缓存为2
        LRUCacheSimple cache = new LRUCacheSimple(2);
        cache.put(1,1);
        cache.put(2,2);
        // 这时缓存已满，返回值1
        System.out.println(cache.get(1));
        // 已满，将最少使用（2，2）删除，插入（3，3）
        cache.put(3,3);
        // （2，2）使用最少，已被删除，返回-1
        System.out.println(cache.get(2));
        // 已满，将最少使用（1，1）删除，插入（4，4）
        cache.put(4,4);
        // 此时缓存中有（3，3）（4，4），返回-1
        System.out.println(cache.get(1));
        // 返回 3
        System.out.println(cache.get(3));
        // 返回 4
        System.out.println(cache.get(4));
    }

    private final LinkedHashMap<Integer, Integer> map;
    public LRUCacheSimple(int capacity) {
        map = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            // 覆盖重写LinkedHashMap的removeEldestEntry方法，在缓存已满的情况下返回true
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    public int get(int key) {
        // 从缓存中获取key对应的值，若未命中则返回-1
        return map.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        // 向缓存中查询或者更新值
        map.put(key, value);
    }
}
