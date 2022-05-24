package net.laohui;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Configuration
@Component
@EnableScheduling
public class RunExecution implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("-------------->" + "项目启动，现在时间：" + new Date());
    }

}

