package com.example.lhygtavservice;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import sun.misc.VM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhy
 * @date 2024/9/19
 * @apiNote
 **/
public class DebuggerTest {
    private volatile static DebuggerTest debuggerTest;

    private DebuggerTest() {
    }
    public static DebuggerTest getInstance(){
        if (debuggerTest ==null){

            synchronized (DebuggerTest.class){
                   if (debuggerTest ==null){

                       debuggerTest =new DebuggerTest();

                   }


            }


        }

        return debuggerTest;

    }

    public static void main(String[] args) {
        DebuggerTest instance = DebuggerTest.getInstance();


        debugTest();
        debugTest2();





    }


    public static void debugTest(){
        System.out.println("-------1----");
        System.out.println("-------2----");

        System.out.println("-------3----");

        System.out.println("-------4----");

    }


    public static void debugTest2(){

        ArrayList<Integer> lists = Lists.newArrayList();
        for (int i=1;i<=5;i++){
            if (i!=3){
                lists.add(i);
            }
        }
        lists.add(101);
        System.out.println(lists);

        List<Integer> collect = lists.stream()
                .filter(k -> k == 2)
                .map(i -> i * i)
                .collect(Collectors.toList());
        System.out.println(collect);
        //System.gc(); 人工开启gc
    }




}
