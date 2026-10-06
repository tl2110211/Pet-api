package com.example.petapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Pet 实体类（领域模型）
 *
 * 这是一个 POJO（Plain Old Java Object，普通的 Java 对象）
 * 用于表示业务领域中的"宠物"概念
 *
 * 注意：这个类没有任何 Spring 注解，因为它不是 Spring Bean
 * 它只是一个数据载体（Data Transfer Object）
 *
 * 升级：添加了 JPA 注解，让它可以映射到数据库表
 */
@Entity  // 告诉 JPA：这是一个数据库实体，对应数据库中的 pet 表
public class Pet {

    // 私有字段：封装数据，外部只能通过 getter/setter 访问
    @Id  // 主键字段
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自动生成 ID
    private Long id;      // 宠物的唯一标识

    private String name;  // 宠物名称
    private int age;      // 宠物年龄

    /**
     * 无参构造函数
     *
     * 【重要】Spring 在处理 JSON 时需要这个构造函数
     * 工作流程：
     * 1. 客户端发送 JSON：{"name": "旺财", "age": 3}
     * 2. Spring 调用 new Pet() 创建空对象
     * 3. Spring 通过 setter 方法设置属性值
     * 4. 最终得到一个完整的 Pet 对象
     */
    public Pet() {
    }

    /**
     * 全参构造函数
     *
     * Service 层创建新宠物时使用这个构造函数
     * 可以一次性设置所有属性，代码更简洁
     */
    public Pet(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // ============ Getter 方法 ============
    // Spring 将对象转换为 JSON 时会调用这些方法
    // 例如：getId() -> JSON 中的 "id" 字段

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // ============ Setter 方法 ============
    // Spring 将 JSON 转换为对象时会调用这些方法
    // 例如：JSON 中的 "name" 字段 -> setName(...)

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
