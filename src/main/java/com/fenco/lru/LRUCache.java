package com.fenco.lru;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: fenco
 * @Date: 2021/11/16 15:37
 * @ClassName: lru
 * @Description: LRU缓存算法
 * 使用HashMap+双向链表，使get和put的时间复杂度达到O(1)。
 * 读缓存时从HashMap中查找key，更新缓存时同时更新HashMap和双向链表，双向链表始终按照访问顺序排列。
 */
public class LRUCache {

    /**
     * @param args
     * 测试程序，访问顺序为[[1,1],[2,2],[1],[3,3],[2],[4,4],[1],[3],[4]]，其中成对的数调用put，单个数调用get。
     * get的结果为[1],[-1],[-1],[3],[4]，-1表示缓存未命中，其它数字表示命中。
     */
    public static void main(String[] args) {
        // 定义缓存为2
        LRUCache cache = new LRUCache(2);
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


    /**
     * 向缓存中查询或者更新值
     * @param key
     * @param value
     */
    private void put(int key, int value) {
        // 查询map中是否有该key
        if (map.containsKey(key)) {
            Entry entry = map.get(key);
            entry.value = value;
            // 将entry节点移到链表最末端【因为尾节点为访问最新】
            popTotal(entry);
        } else {
            // 未查询到，则移除最老的一条，将新值更新进去
            Entry newEntry = new Entry(key, value);
            // 如果map长度超过容量，则移除最老的一条
            if (map.size() >= capacity) {
                // removeFirst：移除并返回最老一条的key
                Entry first = removeFirst();
                map.remove(first.key);
            }
            // 将entry节点【添加】链表最末端【因为尾节点为访问最新】
            addTotail(newEntry);
            // 更新map
            map.put(key, newEntry);
        }
    }


    /**
     * 从缓存中获取key对应的值，若未命中则返回-1
     * @param key
     * @return
     */
    private int get(int key) {
        if (map.containsKey(key)) {
            Entry entry = map.get(key);
            // 将entry节点移到链表最末端【因为尾节点为访问最新】
            popTotal(entry);
            return entry.value;
        }
        return -1;
    }



    /**
     * 将entry节点【添加】链表最末端【因为尾节点为访问最新】
     * @param entry
     */
    private void addTotail(Entry entry) {
        /*
         * 插入到末尾
         */
        Entry last = tail.prev;
        last.next = entry;
        tail.prev = entry;
        entry.prev = last;
        entry.next = tail;
    }

    /**
     * 移除并返回最老的一条key（即链表首端节点）
     * @return
     */
    private Entry removeFirst() {
        Entry first = head.next;
        Entry second = first.next;
        head.next = second;
        second.prev = head;
        return first;
    }

    /**
     * 将entry节点【移到】链表最末端【因为尾节点为访问最新】
     * @param entry
     */
    private void popTotal(Entry entry) {
        /*
          首先完成Entry与前后链表的切断
         */
        // entry.prev指上一个链表
        Entry prev = entry.prev;
        // entry.next指下一个链表
        Entry next = entry.next;
        // 上一个链表的next指向上一个链表
        prev.next = next;
        // 下一个链表的prev指向上一个链表
        next.prev = prev;

        /*
          插入到末尾
         */
        Entry last = tail.prev;
        last.next = entry;
        tail.prev = entry;
        entry.prev = last;
        entry.next = tail;
    }

    /**
     * 缓存容量
     */
    private final int capacity;

    /**
     * 用于加速缓存项随机访问性能的HashMap
     */
    private HashMap<Integer, Entry> map;

    /**
     * 双向链表头节点，该侧的缓存项访问时间最早
     */
    private Entry head;

    /**
     * 双向链表的尾节点，该侧的缓存项访问最新
     */
    private Entry tail;

    /**
     * 初始化缓存(大小固定，自定义)
     *
     * @param capacity
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<Integer, Entry>((int) (capacity / 0.75 + 1), 0.75f);
        head = new Entry(0, 0);
        tail = new Entry(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * 缓存的包装类，包含键、值、前驱节点、后继节点
     */
    class Entry {
        int key;
        int value;
        Entry prev;
        Entry next;

        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
