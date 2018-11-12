package com.example.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
//@MapperScan(basePackages = "com.example.dynamic.mapper", sqlSessionTemplateRef = "dynamicSqlSessionTemplate")
public class DynamicApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DynamicApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动完成!");
    }

    @RequestMapping("/")
    String home() {
        return "redirect:buy";
    }
}
