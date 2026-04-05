package com.offerflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.offerflow.mapper")
public class OfferFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfferFlowApplication.class, args);
    }
}
