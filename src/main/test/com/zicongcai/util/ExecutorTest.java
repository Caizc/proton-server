package com.zicongcai.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExecutorTest {

    public static void main(String args[]) {
        Executor executor = Executor.getInstance();

        Future futureA = executor.submit(new RunnerA());
        Future futureB = executor.submit(new RunnerB());

        boolean flag = true;

        while (true) {
            try {
                if (futureA.isDone() & flag) {
                    flag = false;
                    System.out.println(futureA.get().toString());
                }

                if (futureB.isDone()) {
                    System.out.println(futureB.get().toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (futureA.isDone() && futureB.isDone()) {
                break;
            }
        }
    }
}
