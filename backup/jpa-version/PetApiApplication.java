package com.example.petapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用程序的入口类
 *
 * 这是整个应用的启动点，类似于传统 Java 程序的 main 方法
 */
@SpringBootApplication  // 【核心注解】这是一个"三合一"注解，包含：
                        // 1. @Configuration：告诉 Spring 这是一个配置类
                        // 2. @EnableAutoConfiguration：让 Spring Boot 自动配置（比如自动配置 Tomcat 服务器）
                        // 3. @ComponentScan：自动扫描这个包及子包下的所有 Spring 组件（@Controller、@Service、@Repository 等）
public class PetApiApplication {

    /**
     * 应用程序的入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        // 这一行代码会：
        // 1. 启动内置的 Tomcat 服务器（默认端口 8080）
        // 2. 扫描并注册所有的 Spring Bean（Controller、Service、Repository）
        // 3. 自动配置数据库连接、JSON 转换器等
        SpringApplication.run(PetApiApplication.class, args);
    }
}
