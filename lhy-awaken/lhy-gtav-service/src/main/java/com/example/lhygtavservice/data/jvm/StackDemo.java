package com.example.lhygtavservice.data.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author lhy
 * @date 2024/9/23    push pop add
 * @apiNote stack栈列 后进先出（Last In, First Out, LIFO）的原则。拿盘子
 **/
@Slf4j
public class StackDemo {
    private static String s ="lhy";
    private static String s1 ="Yz";
    private static String s3 ="go";


    public static void main(String[] args) {
        //simpleDemo();
        javaStackArrayDeque();
      //  javaStackLinkedList();
    }

    public static void simpleDemo(){

        Stack<Object> objects = new Stack<>();
        //往里面放
        objects.push("1");
        objects.push("2");
        log.info(objects.toString());
       //取出栈列最上面的元素 不移除
        log.info(objects.peek().toString());

        //从栈列最上面移除
        objects.pop();
        log.info(objects.toString());
    }
   /*
      用双端队列模拟栈
    */
    public static void javaStackArrayDeque(){
        ArrayDeque<String> stack = new ArrayDeque<>();
        //从顶部加
        stack.push(s);
        stack.push(s1);
        log.info("开始队列："+stack);
        //从尾部加上
        boolean add = stack.add(s1);
        stack.add(s3);
        log.info("现在队列："+stack);
        String first = stack.getFirst();
        log.info("头元素："+first);
        String last = stack.getLast();
        log.info("尾元素："+last);

        log.info("remove被移除的元素是:"+stack.remove());
        String pop = stack.pop();
        log.info("pop被移除的元素是:"+pop);
        log.info(stack.toString());
        log.info("最后队列："+stack);


    }

    /*
       用双向链表模拟队列 同上
     */
    public static void javaStackLinkedList(){
        LinkedList<String> stack = new LinkedList<>();
        stack.push(s);
        boolean add = stack.add(s1);
        stack.push(s3);
        log.info(stack.toString());
        String first = stack.getFirst();
        log.info(first);
        String last = stack.getLast();
        log.info(last);
        stack.remove();
        log.info(stack.toString());




    }
    }

/**
 * @author lhy
 * @date 2024/9/23 offer poll
 * @apiNote 队列 （FIFO, First In First Out）先进先出，排队买票
 **/
@Slf4j
 class QueueDemo {

    public static void main(String[] args) {
        simpleDemo();
        //javaStackLinkedList();
    }



    public static void simpleDemo(){
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.offer(9);
        queue.offer(45);
        queue.add(100);
        //先进先出，取的是1
        Integer poll = queue.poll();
        log.info("被poll取出的元素："+poll);
        queue.add(5);
        Integer remove = queue.remove();
        log.info("被remove取出的元素："+remove);


    }
    public static void javaStackLinkedList(){
        //直接模拟队列
        LinkedList<String> queue = new LinkedList<>();


    }



}
