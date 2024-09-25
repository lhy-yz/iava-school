package com.example.lhygtavservice.data.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * @author lhy
 * @date 2024/9/10
 * @apiNote java lock锁 三个线程购票实例 （异步线程报错不会影响主线程）
 **/
@Slf4j
@Data
public class MonitorLock {
    private static int tickets=50;

    private  ReentrantLock lock = new ReentrantLock();

    public void saleTicket(){
        lock.lock();
        try {
            if(tickets>0){
            log.info(Thread.currentThread().getName()+"第几张票："+(tickets--)+"，剩余票数:"+tickets);
            }else {
                log.info("票卖光了，票数=0："+tickets);
                log.info("-------------------end---------------");
                int i=10/0;
            }

        }finally {
            lock.unlock();
        }


    }




}

@Slf4j
class SaleTicket{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Thread.currentThread().setName("first");
        //new 卖票的对象
        MonitorLock saleTicketsBus = new MonitorLock();
        FutureTask<String> task =   new FutureTask<>(()->{
            for (int i=0;i<51;i++){
                saleTicketsBus.saleTicket();
                log.info(Thread.currentThread().getName()+"线程一买票了");
            }


            return "t1";
        });
        threadPool.submit(task);

        FutureTask<String> task2 =   new FutureTask<>(()->{
            for (int i=0;i<51;i++){
                saleTicketsBus.saleTicket();
                log.info(Thread.currentThread().getName()+"线程二买票了");
            }
            return "t2";
        });



        threadPool.submit(task2);


        FutureTask<String> task3 =   new FutureTask<>(()->{
            for (int i=0;i<51;i++){
                saleTicketsBus.saleTicket();
                log.info(Thread.currentThread().getName()+"线程三买票了");
            }
            return "t3";
        });

        threadPool.submit(task3);
// 异步线程报错并不会影响主线程报错，主线程报错是真的会提示
        threadPool.shutdown();
/*
        for (int i=0;i<51;i++){
            saleTicketsBus.saleTicket();
            log.info(Thread.currentThread().getName()+"线程三买票了");
        }
*/

        TimeUnit.SECONDS.sleep(5);
        log.info(Thread.currentThread().getName()+"---------主线程结束----------------");



    }

}

/**
 * @author lhy
 * @date 2024/9/19
 * @apiNote 读写锁的存在是读写互斥，但读读共享 区别于传统锁，写权限高于读权限 问题（读锁没全释放的时候，写锁拿不到锁）
 **/
class ReadWriteLockDemo {
    public static void main(String[] args) {

        MyResource myResource = new MyResource();
         //10个线程写
        for (int i=1;i<=10;i++){
           int finalI=i;
            new Thread(()->{
                myResource.write(finalI+"",finalI+"");

            },String.valueOf(i)).start();



        }

        //10个线程读
        for (int i=1;i<=10;i++){
            int finalI=i;
            new Thread(()->{
                try {
                    myResource.read(finalI+"");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            },String.valueOf(i)).start();



        }


    }
}


@Slf4j
class MyResource{
    Map<String,String> map=   new HashMap<>();
    ReentrantLock lock = new ReentrantLock();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void write(String key,String value){
        readWriteLock.writeLock().lock();
        try {
            log.info(Thread.currentThread().getName()+",正在写入");
           map.put(key,value);
            log.info(Thread.currentThread().getName()+",完成写入");
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

public void read(String key) throws InterruptedException {
    Lock readLock = readWriteLock.readLock();
    readLock.lock();
    try {
        log.info(Thread.currentThread().getName()+",正在读入");
        String result = map.get(key);
       // TimeUnit.MILLISECONDS.sleep(200);
        log.info(Thread.currentThread().getName()+",完成读取,"+result);
    }  finally {
        readLock.unlock();
    }

}


}



/**注意啊 不支持重入！！！
 * 1.8新增读写锁 对可重入读写锁强化
 *  -----最强java锁-----  写锁可以冲入读锁  写锁可以进入乐观读锁
 */
@Slf4j
class StampLockDemo {
    public static void main(String[] args) {
        StampedLock stampedLock = new StampedLock();
        long oldRead = stampedLock.readLock();
        stampedLock.unlock(oldRead);
 log.info("------上述是传统读锁 写锁进不去----");

        //乐观读锁。写锁可以进入
        long leguanRead = stampedLock.tryReadLock();
        //判断validate是否因为写锁 发生改变
        boolean validate = stampedLock.validate(leguanRead);
        //当validate 改变时可对读锁升级
        leguanRead=stampedLock.readLock();

        stampedLock.unlock(leguanRead);

        log.info("------上述是乐读锁 写锁可以进去 之后进行读锁升级 并释放----");

    }


}
