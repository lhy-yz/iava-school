package com.example.lhygtavservice.data.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lhy
 * @date 2024/9/10
 * @apiNote 线程中断3种机制 volatile，AtomicBoolean， interuppted
 **/

@Slf4j
public class ThreadStop {
    static volatile  boolean isStop=false;
    static AtomicBoolean atomicStop=new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {



        Thread t1 = new Thread(() -> {
           while (true){
               if (isStop){
                   log.info(Thread.currentThread().getName()+"标识符被修改 线程即将终止");
                   break;
               }
               log.info("持续输出-----");
           }
        }, "t1");
        t1.start();


        TimeUnit.SECONDS.sleep(1);
        log.info("开始中断标识符----");
        //t1.isInterrupted()
        // atomicStop.set(true);
        Thread t2 = new Thread(() -> {
            isStop = true;
        }, "t2");
          t2.start();

        log.info("中断成功----");


    }
}

class LockConditionDemo{
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("-----------come in--------------");
                //lock的暂时睡眠  =wait 只能在lock和unlock下执行
                condition.await();
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
            System.out.println("-----------被唤醒--------------");
        }, "t1");
        t1.start();

        new Thread(()->{
            //locd的唤醒   =notify
            condition.signal();
            System.out.println("-----------发方通知--------------");
        },"t2").start();





    }

}


class LockSupportDemo{

    public static void main(String[] args) {
        //最优秀的等待和唤醒=>拦截 高于wait,notify，发放通行证

        Thread t1 = new Thread(() -> {
            System.out.println("-----------come in- 准备挂起-------------");
            LockSupport.park();
            System.out.println("-----------被唤醒了 继续执行--------------");

        }, "t1");
                t1.start();

               new Thread(()->{
                   LockSupport.unpark(t1);
               System.out.println("-----------发方通知--------------");
               },"t2").start();



    }





}


 class ThreadWaitAndNotifyDemo {
     // 创建一个将被两个线程同时访问的共享对象
     public static Object object = new Object();

     // Thread0线程，执行wait()方法
     static class Thread0 extends Thread {

         @Override
         public void run() {
             synchronized (object) {
                 System.out.println(Thread.currentThread().getName() + "初次获得对象锁，执行中，调用共享对象的wait()方法...");
                 try {
                     // 共享对象wait方法，会让线程释放锁。
                     object.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 System.out.println(Thread.currentThread().getName() + "再次获得对象锁，执行结束");
             }
         }

     }

     // Thread1线程，执行notify()方法
   static   class Thread1 extends Thread {

         @Override
         public void run() {
             synchronized (object) {
                 // 线程共享对象，通过notify()方法，释放锁并通知其他线程可以得到锁
                 object.notify();
                 System.out.println(Thread.currentThread().getName() + "获得对象锁，执行中，调用了共享对象的notify()方法");
             }
         }
     }

     // 主线程
     public static void main(String[] args) {
         Thread0 thread0 = new Thread0();
         Thread1 thread1 = new Thread1();
         thread0.start();
         try {
             // 保证线程Thread0中的wait()方法优先执行，再执线程Thread1的notify()方法
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         thread1.start();

/*         运行结果
         Thread-0初次获得对象锁，执行中，调用共享对象的wait()方法...
         Thread-1获得对象锁，执行中，调用了共享对象的notify()方法
         Thread-0再次获得对象锁，执行结束*/

     }

 }

