package com.example.petapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Pet 实体类（普通版本）
 *
 * 这是一个普通的 Java 类（POJO）
 * 现在还没有和数据库关联
 */
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    // Spring 需要的无参构造方法
    public Pet() {
    }

    // 创建对象时使用
    public Pet(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
