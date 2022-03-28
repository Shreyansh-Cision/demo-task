package com.example.demotask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.stereotype.Component;

@EnableTask
@Component
public class DemoTask implements CommandLineRunner {

    private static final Logger LOGGER = LogManager.getLogger(DemoTask.class);

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i<100;i++) {
            LOGGER.info("DemoTask :: Performing Task {}",i+1);
            Thread.sleep(1000);
        }
    }
}
