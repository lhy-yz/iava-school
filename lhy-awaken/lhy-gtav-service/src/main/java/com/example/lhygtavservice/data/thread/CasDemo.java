package com.example.lhygtavservice.data.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lhy
 * @date 2024/9/11
 * @apiNote cas 机制
 * CAS是一条CPU的原子指令，底层是给总线程枷锁，也就是CAS的原子性实际是CPU实现独占
 **/
@Slf4j
@Data
@AllArgsConstructor
public class CasDemo {
    private String username;
    private  int age;

    public static void main(String[] args) {
        //被native修饰的 是操作系统C++指令直接调用 保证原子性 结合volatile 性能大于sysnachoried
        Unsafe unsafe = Unsafe.getUnsafe();


        AtomicInteger atomicInteger = new AtomicInteger(5);
        atomicInteger.compareAndSet(5,2022);
        log.info(String.valueOf(atomicInteger.get()));
     //   unsafe.compareAndSwapInt(this, valueOffset, expect, update); 当前对象 当前对象的偏移量地址 目标的预计值 如果是true 就修改

    }


}
@Slf4j
class AtomicReferenceDemo{
    public static void main(String[] args) {
        //atomic 专用对象
        AtomicReference<CasDemo> reference = new AtomicReference<>();
        CasDemo yz = new CasDemo("yanzi",26);
        CasDemo lhy = new CasDemo("lhy",28);

        reference.set(yz);
        reference.set(lhy);
        boolean b = reference.compareAndSet(yz, lhy);
        log.info(reference.toString());
    }


}

@Slf4j
class ThreadChuanZhiDemo{

    public static void main(String[] args) throws InterruptedException {
           AtomicInteger a  = new AtomicInteger(3);

         new Thread(()->{
              a.set(4);

         },"t1").start();

        new Thread(()->{
            log.info("a:"+a.get());
            a.set(29);
          //  a.compareAndSet(a.get(),28);
        },"t2").start();

        TimeUnit.SECONDS.sleep(2);
        log.info(Thread.currentThread().getName()+":a"+a.get());

    }



}

@Slf4j
 class SpinLockDemo{
     AtomicReference<Thread> atomicLock =   new AtomicReference<Thread>();

    public  void lock()  {

        Thread thread = Thread.currentThread();
        //刚进来开始占位
        log.info(Thread.currentThread().getName()+"--拿锁 come in---");
        while (!atomicLock.compareAndSet(null, thread)){
                   //a进来占位 flag=true, a首次不不满足while循环，
                   // b过一秒后进来抢位置 锁拿不到  flag=false !false=true 进入while循环
                   //a 5s进行释放 null b才拿到锁 null->threadb   flag=true,占位成功
                   // a必须在b前面执行
            log.info(Thread.currentThread().getName()+"-------无法拿到锁被搁置 无法跳出while------");

        };
        log.info(Thread.currentThread().getName()+"-------拿到锁，执行自己线程的逻辑------");

    }

     public  void unlock(){
         Thread thread = Thread.currentThread();
         // 如果线程相同  释放锁
         boolean nFlag = atomicLock.compareAndSet(thread, null);
         log.info(Thread.currentThread().getName()+"--释放锁 come out---"+nFlag);


     }


    public static void main(String[] args) throws InterruptedException {
        SpinLockDemo spinLock = new SpinLockDemo();

        new Thread(()->{
                spinLock.lock();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            spinLock.unlock();
        },"a").start();


        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            spinLock.lock();
            spinLock.unlock();
        },"b").start();




    }




     }