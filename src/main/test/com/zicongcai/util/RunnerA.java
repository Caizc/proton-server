package com.zicongcai.util;

import java.util.concurrent.Callable;

class RunnerA implements Callable<Object> {

    @Override
    public Boolean call() throws Exception {

        for (int i = 0; i < 10; i++) {
            System.out.println("1");
            Thread.sleep(50);
        }

        return true;
    }
}
