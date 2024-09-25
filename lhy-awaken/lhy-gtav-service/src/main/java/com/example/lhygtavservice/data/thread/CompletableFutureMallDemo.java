package com.example.lhygtavservice.data.thread;

import com.example.lhycommonservice.eneity.Product;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author lhy
 * @date 2024/9/9
 * @apiNote FutureTask和compltableFuture的高级运用 when then
 **/
/*
高阶版编程
 */
@Slf4j
public class CompletableFutureMallDemo {
    static List<NetMall> list=Lists.newArrayList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("taobao")

    );

    /**
     * 集合遍历 多线程并发查询
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list,String productName){
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        //查询三条=一条用map转
        List<CompletableFuture<String>> futures = list.stream().map(mall ->
                CompletableFuture.supplyAsync(() ->{ //转化为固定格式
                    String.format(productName + "in %s price is %.2f", mall.getNetMallName(), mall.calcPrice(productName));
                     log.info("bbbb:"+Thread.currentThread().getName());
                            return null;
                        },threadPool)
                .thenApply(f -> {
                    //串行化 从上到下 继续执行 类似case when then
                    log.info("----继续:"+Thread.currentThread().getName());
                    //查询一条相当于三条
                    return "";
                })
        ).collect(Collectors.toList());

        threadPool.shutdown();
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> mysql = getPrice(list, "mysql");

        long endTime = System.currentTimeMillis();
     log.info("时间："+(endTime-startTime));
        log.info( String.valueOf(ThreadLocalRandom.current().nextDouble()*2+"mysql".charAt(0)));


    }

}

/*
电商网站
 */
@Data
@AllArgsConstructor
class NetMall {
    private String netMallName;

    public BigDecimal calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
        }
        double v = ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
        return BigDecimal.valueOf(v);
    }
}


/**
 * 5.最高阶常用 线程池ThreadPool结合completablefuturetask
 */
@Slf4j
@Accessors(chain = true)//类似与build注解
class ThreadPoolCompleteFutureDemo {
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
        }, threadPool).whenComplete((v, e) -> {
            log.info("v:" + v);

        }).exceptionally(e -> {
            log.info("异常情况：" + e.getMessage());
            return e.getMessage();
        });
        log.info(futureTask.join());//join=get 但不会抛出异常
        //关闭线程池
        threadPool.shutdown();
    }

}
/**
 * completableFutur 多种情况的运用 练习版
 */
@Slf4j
class CompletableFuturePlus{

    public static void main(String[] args) {
        List<Product> ls= Lists.newArrayList();
          //可以不用自定义线程池，系统会默认FoJoJoinPooL
        CompletableFuture<List<Product>> cs=CompletableFuture.supplyAsync(()->{
                    //逻辑
                    log.info("-----查询返回集合-----");

                    return ls;
                }).thenApply(f->{
                    //从上倒下串行化

                    return f;
                })
                .whenComplete((list,v)->{

                })/*.thenRunAsync(()->{
                       开启另一个线程 返回void
                })*/
                .exceptionally(e->{

                    return ls;
                });

        List<Product> join = cs.join();
    }

}

/**
 * completablecombin 合并结果 线程1合并线程2 通过return
 */
@Slf4j
class completableCombin1{
    //合并
    public static void main(String[] args)throws ExecutionException, InterruptedException {
        //主线程
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(()->{
            //第一个线程
            System.out.println(Thread.currentThread().getName()+"\t"+"---come in 1");
            return 10;
        },threadPool).thenCombine(CompletableFuture.supplyAsync(()-> {
            //第二个线程
            System.out.println(Thread.currentThread().getName()+ "\t"+ "---come in 2");
            return 20;})
                ,(x,y)->{
                    //主线程
                    System.out.println(Thread.currentThread().getName()+ "\t" + "---come in 3");
            return x+ y;
            }).thenCombine(CompletableFuture.supplyAsync(()->{
            //第二个线程
            System.out.println(Thread.currentThread().getName()+ "\t" +"---come in 4");
            return 30;
            }),(a,b)->{
            //主线程

            System.out.println(Thread.currentThread().getName()+"\t"+ "---come in 5");
            return a+ b;
        }).thenCombine( CompletableFuture.supplyAsync(()->{
            //第二个线程
            System.out.println(Thread.currentThread().getName()+ "\t" +"---come in 6");
            return 30;}),(a,b)->{
            //主线程

            System.out.println(Thread.currentThread().getName()+"\t"+ "---come in 7");
            return a+ b;});

        //主线程

        System.out.println(Thread.currentThread().getName()+"-----主线程结束，END");
        System.out.println(thenCombineResult.get());
        threadPool.shutdown();
    }

}


