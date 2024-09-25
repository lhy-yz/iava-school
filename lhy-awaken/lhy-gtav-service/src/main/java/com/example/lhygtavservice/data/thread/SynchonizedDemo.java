package com.example.lhygtavservice.data.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author lhy
 * @date 2024/9/11
 * @apiNote 测试Synchonized同步锁 作用的区域范围
 **/
public class SynchonizedDemo {
    // 结论1. 同一资源类下 synchronized修饰与synchronized修饰发生竞争 没有synchronized修饰 则不参与正常执行
     //    2 。staic修饰的是类锁 不与synchronized修饰的对象进行竞争
    //   3.同为 staic修饰的 相互之间进行竞争 正常结论 先选的线程优先

}

@Slf4j
class Phone{
    public synchronized void sendEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);

        log.info("----send email");

    }
    public synchronized void sendSMS(){
        log.info("----send SMS");

    }

    public  void hello(){
        log.info("----send hello");

    }

    public static synchronized void sendEmail2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);

        log.info("----send email2");

    }

    public static synchronized void sendSms2(){
        log.info("----send SMS2");

    }
}

class Lock8Demo{
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        Phone phone2 = new Phone();
        //线程一与线程2竞争锁对象的资源
           new Thread(()->{
               try {
                   phone.sendEmail();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           },"t1").start();


        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            phone.hello();
        },"t2").start();

    }


}