package com.example.lhygtavservice.data.thread;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author lhy
 * @date 2024/9/7
 * @apiNote 4种线程创建 简单到高阶 thread,runnable,callabel,excutors,(completableFuture)
 **/
@Slf4j
public class TestThread {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            log.info(Thread.currentThread().getName()+"开始运行"+(Thread.currentThread().isDaemon()?"守护线程":"用户线程"));

        },"t1");
        thread.setDaemon(true);
        thread.start();
        log.info(Thread.currentThread().getName()+"开始运行2"+(Thread.currentThread().isDaemon()?"守护线程":"用户线程"));


    }
}

/**
 * 1.继承thread类
 */
class MyThread0 extends Thread{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println( Thread.currentThread().getName()+": 开始执行线程--wo awd ad ");
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread0 myThread0 = new MyThread0();
        MyThread0 myThread1 = new MyThread0();
        myThread0.start();
        myThread1.start();
        System.out.println( Thread.currentThread().getName()+"---pmain----");
        System.out.println("sdadas222222222222dqe123");
        System.out.println("sdadas222222222222dqe123");
        TimeUnit.SECONDS.sleep(2);


    }

}
/**
 * 2.实现runnable 接口
 */
class MyThread implements Runnable {

    @Override
    public void run() {
        System.out.println("---runnable 开始run----");
        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName()+"---runnable 执行结束---");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    public static void main(String[] args) throws InterruptedException {

        MyThread myThread = new MyThread();
        System.out.println("---这是一个runnable----");
        System.out.println( Thread.currentThread().getName()+"---main----");
        Thread thread = new Thread(myThread);
        thread.start();
        TimeUnit.SECONDS.sleep(5);
        System.out.println( Thread.currentThread().getName()+"---main主线程？？？----");

    }

}
/**
 * 2.实现runnable 接口 lambdb表达式 常用
 */
class MyThread01 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {

        }, "t1");

        t1.start();
    }
}


/**
 * 2.实现callable 接口 结合futuretask
 */
@Slf4j
class MyThread2  implements Callable<String>{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(new MyThread2());
        Thread t1 = new Thread(task,"t1");
        t1.start();
        log.info("结果:"+task.get());


    }

    @Override
    public String call() throws Exception {
        log.info("name:"+Thread.currentThread().getName());
        return "hello callable";
    }
}



/**
 * 4.最常用 线程池ThreadPool结合futuretask
 */
@Slf4j
class ThreadPoolFutureTask {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //FutureTask 结合线程池 测试3个线程 2个做异步 用线程池就用不上thread 节约资源

            ExecutorService executorService = Executors.newFixedThreadPool(3);
        //线程1
        FutureTask<String> task = new FutureTask<String>(() -> {
            TimeUnit.MILLISECONDS.sleep(300);
            log.info(Thread.currentThread().getName()+":开始运行-------------");
            return "success t1";
        });
        executorService.submit(task);
        FutureTask<String> task2 = new FutureTask<String>(() -> {
            TimeUnit.MILLISECONDS.sleep(300);
            log.info(Thread.currentThread().getName()+":开始运行-------------");
            return "success t2";
        });
        executorService.submit(task2);

        //线程3 直接用主线程

        TimeUnit.MILLISECONDS.sleep(300);
        log.info(Thread.currentThread().getName()+":开始运行-------------");
        //关闭线程池
        executorService.shutdown();
        log.info(task2.get());
        log.info(task.get());
    }

}

/**
 * 5.最高阶常用 线程池ThreadPool结合completablefuturetask
 */
@Slf4j
@Accessors(chain = true)//类似与build注解
class ThreadPoolCompleteFuture{
    //用CompletableFuture是因为其get方法不堵塞 ，Future堵塞 对异步的处理很强大
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        //如果线程池不指定 系统会默认Forkjoin的线程池
        CompletableFuture<String> futureTask = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log.info(Thread.currentThread().getName() + ":开始运行-------------");
            return "seccess async";
        },threadPool).whenComplete((v,e)->{
            log.info("v:"+v);

        }).exceptionally(e->{
            log.info("异常情况："+e.getMessage());
            return e.getMessage();
        });
        log.info(futureTask.join());//join=get 但不会抛出异常
        //关闭线程池
        threadPool.shutdown();
    }







}