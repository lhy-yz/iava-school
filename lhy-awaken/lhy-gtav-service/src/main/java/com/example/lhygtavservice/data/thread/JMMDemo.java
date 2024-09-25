package com.example.lhygtavservice.data.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author lhy
 * @date 2024/9/11
 * @apiNote 线程共享变量 （线程之际无法访问对方线程工作间的变量）涉及happen-before
 **/
public class JMMDemo {

    public static void main(String[] args) {

    }
}
/**可见性和有序性（禁止重排），
 * 读写 ：先写后读原理：修改完成后立即刷新会主内存 发出一个通知 其他所有线程都是可见的
 * 凭什么能保证可见性和有序性-内存屏障memory
 * Volitale 因为有内存屏障的加入，写是直接存取到主内存 ，读是直接从主内存里获取写之前的最新结果
 */

@Slf4j
class VolitaleDemo{
    /*
    unsafe下面的内存屏障(底层都是c语言的指令 四大屏障)
    读屏障loadFence
    写屏障storeFence
     */

    private volatile int value=1;
    static volatile   boolean flag=true;
    public int getValue() {
        return value;
    }
   //高并发读多写少可以用
    public synchronized void setValue(int value) {
        this.value = value;
    }

    public static void main(String[] args) throws InterruptedException {

        new Thread(()->{
            log.info(Thread.currentThread().getName()+"----come in---");
            while (flag){
              // log.info(Thread.currentThread().getName()+"状态："+flag);

            }
            log.info(Thread.currentThread().getName()+"修改成功"+flag+"修饰符发生改变");
        },"t1").start();

        TimeUnit.SECONDS.sleep(2);

        flag=false;
        log.info(Thread.currentThread().getName()+"---结束---"+flag+"修饰符发生改变");


    }

}

/**
 * 多线程下的单例模式 最好加上volatile
 */
class SafeDoubleCheckSingleton{
    private volatile static  SafeDoubleCheckSingleton singleton;

    //运用单例模式 私有化构造器
    private SafeDoubleCheckSingleton() {
    }
   //重写get方法 运用双端锁 抗住多线程
    public static SafeDoubleCheckSingleton getSingleton() {
        if(singleton==null){
            synchronized (SafeDoubleCheckSingleton.class){
                if (singleton==null){
                    singleton=new SafeDoubleCheckSingleton();
                }
            }
        }

        return singleton;
    }


}