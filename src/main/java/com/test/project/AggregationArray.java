package com.test.project;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 多数组数据扁平化处理
 *
 * @author sunshine
 */
@EqualsAndHashCode
@ToString
public class AggregationArray {

    /**
     * 当前对象的全部数值
     */
    private AtomicInteger atomicInteger;

    /**
     * 推送下标,用于处理map的value集合使用
     */
    private AtomicInteger pushInteger;

    private Map<Integer, List<String>> map;

    public AggregationArray() {
        init();
    }

    private void init() {
        this.atomicInteger = new AtomicInteger(0);
        this.pushInteger = new AtomicInteger(0);
        this.map = new LinkedHashMap(16);
    }

    /**
     * map放置的规则:
     * 参数的list计算出长度,将原本的长度+计算list的长度作为key,value就是当前数组
     * <p>
     * 例子:
     * list1: 0,1
     * list2: 7,8,9
     * list3: 4
     * -->
     * map:
     * k:0  v:list1(0,1)
     * k:2  v:list1(7,8,9)
     * k:5  v:list1(4)
     * <p>
     * 那么,下一个key肯定是k6开始
     *
     * @param list
     */
    public void join(List<String> list) {
        map.put(atomicInteger.getAndAdd(list.size()), list);
    }

    public List<String> get() {
        //List<String> finalList = new ArrayList<>(atomicInteger.get());
        //map.values().forEach(finalList::addAll);
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }


    /**
     * 赋值操作
     * 参数长度必须跟当前集合中已放置的数值相同
     *
     * 循环遍历map
     * 依据join的规则,依次截取集合上面的数值,做重新赋值的操作
     *
     * @param list
     */
    public void pull(List<String> list) {
        if (list.size() != atomicInteger.get()) {
            throw new RuntimeException("参数有问题");
        }
        map.forEach((k, v) -> map.put(k, list.subList(k, k + v.size())));
    }

    public List<String> push() {
        Collection<List<String>> values = map.values();
        int incrementAndGet = pushInteger.getAndIncrement();
        int i = 0;
        for (Iterator<List<String>> pairs = values.iterator(); pairs.hasNext(); i++) {
            if (i == incrementAndGet) {
                return pairs.next();
            } else {
                pairs.next();
            }
        }
        return null;
    }


    public void clear() {
        init();
    }
}
