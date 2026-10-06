package com.example.petapi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 创建宠物的请求对象（DTO - Data Transfer Object）
 *
 * 【为什么不直接用 Pet 类？】
 * 因为创建时客户端不需要提供 ID（ID 由服务器生成）
 * 使用专门的 Request 类可以：
 * 1. 明确客户端需要提供哪些字段
 * 2. 防止客户端篡改 ID
 * 3. 让 API 接口更清晰
 *
 * 【客户端发送的 JSON 示例】
 * {
 * "name": "小黑",
 * "age": 2
 * }
 *
 * 注意：没有 id 字段！
 */
public class PetCreateRequest {
    @NotBlank(message = "Pet name cannot be null or blank")
    private String name; // 宠物名称（必填）
    @Min(value = 0, message = "Pet age cannot be negative")
    private int age; // 宠物年龄（必填）

    /**
     * 无参构造函数
     *
     * Spring 需要它来从 JSON 创建对象
     * 工作流程（@RequestBody 的魔法）：
     * 1. 客户端 POST 请求：{"name": "小黑", "age": 2}
     * 2. Spring 调用 new PetCreateRequest()
     * 3. Spring 调用 setName("小黑")
     * 4. Spring 调用 setAge(2)
     * 5. 对象创建完成，传给 Controller 方法
     */
    public PetCreateRequest() {
    }

    // ============ Getter 方法 ============

    /**
     * 获取宠物名称
     *
     * Service 层会调用这个方法获取数据
     */
    public String getName() {
        return name;
    }

    /**
     * 获取宠物年龄
     */
    public int getAge() {
        return age;
    }

    // ============ Setter 方法 ============

    /**
     * 设置宠物名称
     *
     * Spring 解析 JSON 时会调用这个方法
     * JSON 中的 "name" 字段 -> setName(...)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置宠物年龄
     *
     * JSON 中的 "age" 字段 -> setAge(...)
     */
    public void setAge(int age) {
        this.age = age;
    }
}