package com.example.lhygtavservice.data.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lhy
 * @date 2024/9/23
 * @apiNote 堵塞队列 相较于普通队列 填加了put。take 对于线程堵塞的优化
 *阻塞队列常用于生产者和消费者的场景，生产者是往队列里添加元素的线程，消费者是从队列里拿元素的线程。阻塞队列就是生产者存放元素的容器，而消费者也只从容器里拿元素。
（1）当队列中没有数据的情况下，消费者端的所有线程都会被自动阻塞（挂起），直到有数据放入队列。
（2）当队列中填满数据的情况下，生产者端的所有线程都会被自动阻塞（挂起），直到队列中有空的位置，线程被自动唤醒。

 优先选择ArrayBlockingQueue！！！
 **/
@Slf4j
public class BlockQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        //  testArrayBlockQueue(true);
        testLinkedBlockQueue(2);

    }

    /**
     * ArrayBlockQueue
     * @param fair
     */
    static void testArrayBlockQueue( boolean fair) throws InterruptedException {
        //公平堵塞队列 公平是有顺序
        ArrayBlockingQueue fairQueue = new ArrayBlockingQueue(2000, fair);

        boolean offer = fairQueue.offer(1);
        fairQueue.offer("Sdad");
        fairQueue.offer("yz");
        fairQueue.put("ai");
        log.info("刚开始堵塞队列："+fairQueue.toString());

        Object take = fairQueue.take();
        log.info("被take拿走:"+take);
        Object poll = fairQueue.poll();
        log.info("被poll拿走:"+poll);

        log.info("现在堵塞队列："+fairQueue.toString());


    }


    /**
     * LinkedBlockQueue
     * @param size
     */
    static void testLinkedBlockQueue(int size) throws InterruptedException {
        // 默认一个类似无限大小的容量（Integer.MAX_VALUE）。这样一来，如果生产者的速度一旦大于消费者的速度，也许还没有等到队列满阻塞产生，系统内存就有可能已被消耗殆尽了
        LinkedBlockingQueue<Object> linkQueue = new LinkedBlockingQueue<>(size);
        //若 BlockingQueue 为空，则阻断进入等待状态，直到 BlockingQueue 有新的数据被加入。
        //   linkQueue.take();  空集合会堵塞
        linkQueue.offer(2);
        linkQueue.offer(3);
        //4放不进去
        linkQueue.offer(4);
        log.info("刚开始Linked堵塞队列："+linkQueue.toString());
        //linkQueue.put(4); //空间满了合会堵塞



    }

}
