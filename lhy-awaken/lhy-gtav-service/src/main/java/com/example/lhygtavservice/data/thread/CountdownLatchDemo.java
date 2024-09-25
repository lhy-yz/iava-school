package com.example.lhygtavservice.data.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lhy
 * @date 2024/9/18
 * @apiNote 50个线程
 **/
@Slf4j
public class CountdownLatchDemo {
    public static final int size=50;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(size);
        MyNumber myNumber = new MyNumber();
        for (int i=1;i<=size;i++){
            new Thread(()->{
                for (int j=1;j<=1000;j++){

                    myNumber.addPlus();

                }//递减计数器
                countDownLatch.countDown();
            },String.valueOf(i)).start();

        }
        //程序执行玩后拿到结果
        countDownLatch.await();
        log.info(Thread.currentThread().getName()+","+myNumber.getAt().get());



    }


}

@Data
class MyNumber{
 AtomicInteger at=   new AtomicInteger();

 public void addPlus(){
     at.getAndIncrement();

 }

}