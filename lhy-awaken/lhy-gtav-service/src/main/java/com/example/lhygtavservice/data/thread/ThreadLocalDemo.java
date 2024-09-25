package com.example.lhygtavservice.data.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lhy
 * @date 2024/9/18
 * @apiNote
 **/
class  House{
    int saleCount=0;
    public synchronized void saleHouse(){
         saleCount++;
    }
    ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 0);

    //AtomicInteger local=new AtomicInteger();

   public void threadLocalSaleHouse(){
     //销售量+1
       local.set(1+local.get());
   }

}
@Slf4j
public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        House house = new House();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i=0;i<=5;i++){
            new Thread(()->{
                int size = new Random().nextInt(5);
                try {
                    for(int j=1;j<=size;j++){
                        house.saleHouse();
                        house.threadLocalSaleHouse();
                    }//thread的值只是当前线程所赋值 与其他线程毫无相关
                    log.info(Thread.currentThread().getName()+","+house.local.get());
                    //countdouwn 必须使用在线程里
                }finally {
                    //countdown 应该在finally中被调用
                    countDownLatch.countDown();
                    house.local.remove();
                }


            }).start();


        }
        countDownLatch.await();

        log.info(","+house.saleCount);



    }
}


class ThreadLocalDemo2{

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.submit(()->{

        });


    }


}