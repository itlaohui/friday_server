package net.laohui;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableDubbo
@EnableCaching
@MapperScan("net.laohui.dao")
public class FridayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FridayServiceApplication.class, args);
    }
}
