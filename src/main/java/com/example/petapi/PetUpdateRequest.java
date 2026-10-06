package com.example.petapi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 更新宠物的请求对象（DTO - Data Transfer Object）
 *
 * 【为什么不直接用 Pet 类？】
 * 因为更新时：
 * 1. ID 从 URL 路径获取（@PathVariable），不在请求体中
 * 2. 客户端只需提供要修改的字段（名称、年龄）
 * 3. 使用专门的 Request 类让接口更清晰、更安全
 *
 * 【完整的更新请求示例】
 * PUT http://localhost:8080/pets/1 <- ID 在 URL 中
 * Content-Type: application/json
 *
 * { <- 请求体只有要更新的字段
 * "name": "旺财改名",
 * "age": 4
 * }
 *
 * 注意：这个类和 PetCreateRequest 结构相同
 * 但语义不同：一个用于"创建"，一个用于"更新"
 * 分开定义让代码意图更明确，未来也可以独立演化
 */
public class PetUpdateRequest {
    @NotBlank(message = "Pet name cannot be null or blank")
    private String name; // 新的宠物名称
    @Min(value = 0, message = "Pet age cannot be negative")
    private int age; // 新的宠物年龄

    /**
     * 无参构造函数
     *
     * Spring 需要它来从 JSON 创建对象
     *
     * 【更新流程】
     * 1. 客户端 PUT /pets/1：{"name": "新名字", "age": 5}
     * 2. Spring 从 URL 提取 petId = 1 (@PathVariable)
     * 3. Spring 从请求体创建 PetUpdateRequest 对象 (@RequestBody)
     * 4. Controller 调用 service.updatePet(1, updateRequest)
     * 5. Service 找到 ID=1 的宠物，更新它的属性
     */
    public PetUpdateRequest() {
    }

    // ============ Getter 方法 ============

    /**
     * 获取新的宠物名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取新的宠物年龄
     */
    public int getAge() {
        return age;
    }

    // ============ Setter 方法 ============

    /**
     * 设置新的宠物名称
     *
     * Spring 解析 JSON 时调用
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置新的宠物年龄
     *
     * Spring 解析 JSON 时调用
     */
    public void setAge(int age) {
        this.age = age;
    }
}
