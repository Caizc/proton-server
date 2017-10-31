package com.zicongcai.thirdparty;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * 自定义JUnit4ClassRunner,用于实现测试类初始化加载log4j
 */
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:conf/log/log4j.properties");
        } catch (FileNotFoundException e) {
            System.err.println("Cannot Initialize log4j!");
        }
    }

    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
}
