package com.zicongcai.util;

import java.util.concurrent.Callable;

class RunnerB implements Callable<Object> {

    @Override
    public Boolean call() throws Exception {

        for (int i = 0; i < 10; i++) {
            System.out.println("0");
            Thread.sleep(100);
        }

        return false;
    }
}
