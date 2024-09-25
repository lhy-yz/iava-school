package com.example.lhygtavservice.data.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.print.Book;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author lhy
 * @date 2024/9/18
 * @apiNote 高并发下如何控制版本号
 **/

@Slf4j
public class AtomicStampReferenceDemo {

    public static void main(String[] args) {
        Books javabook = new Books(1, "javabook");
        //(对象值，版本号)
        AtomicStampedReference<Books> stampedReference = new AtomicStampedReference<>(javabook, 1);
        log.info(stampedReference.getReference()+",版本："+stampedReference.getStamp());
        Books mysqlbook = new Books(2, "mysqlbook");
        stampedReference.compareAndSet(javabook,mysqlbook, stampedReference.getStamp(), stampedReference.getStamp()+1);
        log.info(stampedReference.getReference()+",版本："+stampedReference.getStamp());

    }


}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Books{
    private int id;
    private  String bookName;

}
/**
 * @author lhy
 * @date 2024/9/18
 * @apiNote 高并发下一次性标记为 true/false
 **/
@Slf4j
class  AtomicMarckedReferenceDemo {
   static AtomicMarkableReference reference=new AtomicMarkableReference(100,false);
  static   CountDownLatch c=  new CountDownLatch(1);
    static   CountDownLatch c2=  new CountDownLatch(1);
    static   CountDownLatch c3=  new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {


        new Thread(()->{
            boolean marked = reference.isMarked();
            log.info(Thread.currentThread().getName()+"标识为："+marked);
            reference.compareAndSet(100,1000,marked,!marked) ;
            log.info(Thread.currentThread().getName()+"更新后标识为："+reference.isMarked());
            c.countDown();
        },"t1").start();


           c.await();
        new Thread(()->{
            log.info(Thread.currentThread().getName());
            boolean marked = reference.isMarked();
            log.info(Thread.currentThread().getName()+"标识为："+marked);
            reference.compareAndSet(100,2000,marked,!marked) ;
            log.info(Thread.currentThread().getName()+","+reference.getReference());
            c2.countDown();
        },"t2").start();
          c2.await();

        new Thread(()->{

            log.info(Thread.currentThread().getName());
            boolean marked = reference.isMarked();
            log.info(Thread.currentThread().getName()+"标识为："+marked+reference.getReference());

            reference.compareAndSet(reference.getReference(),2000,marked,!marked) ;
            log.info(Thread.currentThread().getName()+","+reference.getReference());
            c3.countDown();
        },"t3").start();

        c3.await();
}}

















