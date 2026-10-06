package com.example.petapi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Pet API 控制器（Controller）
 *
 * 职责：接收 HTTP 请求，调用 Service 处理业务，返回 HTTP 响应
 * 这是应用的"前台"，直接面向客户端
 *
 * 【RESTful API 设计】
 * GET    /pets       - 获取所有宠物
 * GET    /pets/{id}  - 获取单个宠物
 * POST   /pets       - 创建新宠物
 * PUT    /pets/{id}  - 更新宠物
 * DELETE /pets/{id}  - 删除宠物
 * GET    /pets/count - 统计宠物数量
 */
@RestController  // 【注解】这是一个"二合一"注解，包含：
                 // 1. @Controller：告诉 Spring 这是一个 Web 控制器
                 // 2. @ResponseBody：自动将返回值转换为 JSON（不需要视图模板）
                 //
                 // 等价于：@Controller + @ResponseBody
public class PetController {

    // 依赖注入：Controller 需要 Service 来处理业务逻辑
    private final PetService petService;

    /**
     * 构造函数注入
     *
     * Spring 会自动注入 PetService 实例
     * 依赖链：Controller -> Service -> Repository
     */
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * 根据 ID 获取单个宠物
     *
     * 【请求示例】GET http://localhost:8080/pets/1
     * 【响应示例】{"id": 1, "name": "旺财", "age": 3}
     *
     * @param petId 从 URL 路径中提取的宠物 ID
     * @return Pet 对象（Spring 自动转换为 JSON）
     */
    @GetMapping(value = "/pets/{petId}", produces = "application/json;charset=UTF-8")
    // @GetMapping：处理 GET 请求的快捷注解，等价于 @RequestMapping(method = RequestMethod.GET)
    // value：URL 路径模板，{petId} 是一个占位符
    // produces：指定响应的内容类型为 JSON，字符集为 UTF-8
    public Pet getPetById(@PathVariable Long petId) {
        // @PathVariable：这就是您问的注解！
        // 作用：把 URL 中的 {petId} 值绑定到方法参数 petId
        // 例如：请求 /pets/123 时，petId 的值就是 123
        // Spring 还会自动做类型转换：字符串 "123" -> Long 类型 123

        return petService.getPetById(petId);
        // Spring 会自动将 Pet 对象转换为 JSON 响应
    }

    /**
     * 获取所有宠物
     *
     * 【请求示例】GET http://localhost:8080/pets
     * 【响应示例】[{"id": 1, "name": "旺财", "age": 3}, {"id": 2, "name": "小白", "age": 1}]
     *
     * @return 宠物列表（Spring 自动转换为 JSON 数组）
     */
    @GetMapping(value = "/pets", produces = "application/json;charset=UTF-8")
    public List<Pet> getPets() {
        return petService.getPets();
    }

    /**
     * 创建新宠物
     *
     * 【请求示例】POST http://localhost:8080/pets
     * Content-Type: application/json
     * Body: {"name": "小黑", "age": 2}
     *
     * 【响应示例】HTTP 201 Created
     * {"id": 3, "name": "小黑", "age": 2}
     *
     * @param newPet 从请求体（JSON）中解析出的对象
     * @return ResponseEntity 包含创建的宠物和 HTTP 状态码
     */
    @PostMapping(value = "/pets", produces = "application/json;charset=UTF-8")
    // @PostMapping：处理 POST 请求（通常用于创建资源）
    public ResponseEntity<Pet> createPet(@RequestBody PetCreateRequest newPet) {
        // @RequestBody：告诉 Spring 从 HTTP 请求体中读取 JSON
        // 工作流程：
        // 1. 客户端发送 JSON：{"name": "小黑", "age": 2}
        // 2. Spring 调用 PetCreateRequest 的无参构造函数
        // 3. Spring 通过 setter 设置属性
        // 4. 最终得到 newPet 对象

        Pet createdPet = petService.createPet(newPet);

        // ResponseEntity：用于自定义 HTTP 响应
        // 这里返回 201 Created 状态码（标准的 REST 实践）
        // 而不是默认的 200 OK
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    /**
     * 删除宠物
     *
     * 【请求示例】DELETE http://localhost:8080/pets/1
     * 【响应示例】HTTP 204 No Content（无响应体）
     *
     * @param petId 要删除的宠物 ID
     * @return ResponseEntity 包含 204 状态码和空响应体
     */
    @DeleteMapping("/pets/{petId}")
    // @DeleteMapping：处理 DELETE 请求（通常用于删除资源）
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);

        // 删除成功后，返回 204 No Content
        // noContent() 返回 204 状态码
        // build() 构建一个空响应体
        //
        // 为什么是 204 而不是 200？
        // - 200 OK：操作成功，响应体有内容
        // - 204 No Content：操作成功，响应体为空（删除操作通常如此）
        return ResponseEntity.noContent().build();
    }

    /**
     * 更新宠物信息
     *
     * 【请求示例】PUT http://localhost:8080/pets/1
     * Content-Type: application/json
     * Body: {"name": "旺财改名", "age": 4}
     *
     * 【响应示例】{"id": 1, "name": "旺财改名", "age": 4}
     *
     * @param petId 要更新的宠物 ID（从 URL 获取）
     * @param updatePet 新的宠物数据（从请求体获取）
     * @return 更新后的 Pet 对象
     */
    @PutMapping(value = "/pets/{petId}", produces = "application/json;charset=UTF-8")
    // @PutMapping：处理 PUT 请求（通常用于更新整个资源）
    public Pet updatePet(@PathVariable Long petId, @RequestBody PetUpdateRequest updatePet) {
        // 注意：这个方法同时使用了两个注解：
        // - @PathVariable：从 URL 获取 petId
        // - @RequestBody：从请求体获取 updatePet
        //
        // 请求流程：
        // 1. URL 提供要更新哪个宠物（ID）
        // 2. 请求体提供新的数据（名称、年龄）
        // 3. Service 执行更新逻辑
        // 4. 返回更新后的对象

        return petService.updatePet(petId, updatePet);
    }

    /**
     * 统计宠物数量
     *
     * 【请求示例】GET http://localhost:8080/pets/count
     * 【响应示例】2
     *
     * @return 宠物总数（Spring 自动转换为 JSON 数字）
     */
    @GetMapping("/pets/count")
    public long countPets() {
        return petService.countPets();
        // 注意：这里返回的是 int 基本类型
        // Spring 会将其转换为 JSON 数字（不是对象）
        // 例如响应体就是：2（而不是 {"count": 2}）
    }
}
