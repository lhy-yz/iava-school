package com.example.lhygtavservice.data.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

/**
 * @author lhy
 * @date 2024/9/18
 * @apiNote   cas+cell分段锁分散扩容 高并发效率最高  LongAccumulator>longAdder>atomicInteger>syschonized
 **/
@Slf4j
public class LongAdderDemo {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        log.info(String.valueOf(longAdder.intValue()));
        //LongAccumulator计算效率最高
        LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 10);
        longAccumulator.accumulate(10);
        log.info(String.valueOf(longAccumulator.get()));

    }
}
