package com.example.petapi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Pet 业务逻辑层（Service）
 *
 * 职责：处理业务逻辑、数据验证、异常处理
 * 这是应用的"大脑"，协调 Controller 和 Repository 之间的工作
 *
 * 【分层架构】
 * Controller -> Service -> Repository
 * ↓          ↓          ↓
 * 接收请求   业务逻辑   数据访问
 */
@Service  // 【注解】告诉 Spring："这是一个业务逻辑层组件，请把它注册为 Bean"
          // 与 @Repository 类似，Spring 会自动管理这个类的实例
public class PetService {

    // 依赖注入：Service 需要使用 Repository 来访问数据
    // 使用 final 确保依赖在构造后不会改变
    private final PetRepository petRepository;

    /**
     * 构造函数注入（推荐方式）
     *
     * 【依赖注入的魔法】
     * Spring 启动时会：
     * 1. 创建 PetRepository 实例
     * 2. 创建 PetService 实例
     * 3. 自动调用这个构造函数，把 PetRepository 实例传进来
     *
     * 这就是"控制反转"（IoC）：
     * - 传统方式：我们自己 new PetRepository()
     * - Spring 方式：Spring 帮我们创建并注入依赖
     *
     * @param petRepository Spring 自动注入的 Repository 实例
     */
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * 获取所有宠物
     *
     * 这个方法很简单，直接委托给 Repository
     * 但在真实项目中，这里可能会：
     * - 添加缓存
     * - 记录日志
     * - 过滤敏感数据
     *
     * @return 所有宠物列表
     */
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    /**
     * 根据 ID 获取单个宠物
     *
     * @param petId 宠物 ID
     * @return 找到的宠物
     * @throws ResponseStatusException 如果宠物不存在，抛出 404 错误
     */
    public Pet getPetById(Long petId) {
        return findPetById(petId);  // 使用私有方法统一处理"找不到"的情况
    }

    /**
     * 统计宠物数量
     *
     * @return 宠物总数
     */
    public long countPets() {
        return petRepository.count();  // JPA 的 count() 返回 long
    }

    /**
     * 创建新宠物
     *
     * 【业务流程】
     * 1. 验证输入数据（名称不能为空、年龄不能为负数）
     * 2. 创建 Pet 对象（不需要手动生成 ID，数据库会自动生成）
     * 3. 保存到数据库
     * 4. 返回保存后的对象（包含数据库自动生成的 ID）
     *
     * @param newPet 客户端提交的宠物数据
     * @return 创建成功的宠物（包含生成的 ID）
     * @throws ResponseStatusException 如果数据验证失败，抛出 400 错误
     */
    public Pet createPet(PetCreateRequest newPet) {
        // 第一步：验证数据
        validatePet(newPet.getName(), newPet.getAge());

        // 第二步：创建 Pet 对象（ID 传 null，让数据库自动生成）
        Pet petToCreate = new Pet(null, newPet.getName(), newPet.getAge());

        // 第三步：保存并返回（save 会返回带 ID 的对象）
        return petRepository.save(petToCreate);
    }

    /**
     * 更新宠物信息
     *
     * 【业务流程】
     * 1. 验证输入数据
     * 2. 查找要更新的宠物（不存在则抛出 404）
     * 3. 更新宠物属性
     * 4. 保存到数据库（JPA 需要显式保存）
     * 5. 返回更新后的对象
     *
     * @param petId 要更新的宠物 ID
     * @param updatePet 新的宠物数据
     * @return 更新后的宠物
     * @throws ResponseStatusException 如果宠物不存在或数据验证失败
     */
    public Pet updatePet(Long petId, PetUpdateRequest updatePet) {
        // 验证数据
        validatePet(updatePet.getName(), updatePet.getAge());

        // 查找宠物（不存在会抛出异常）
        Pet currentPet = findPetById(petId);

        // 更新属性
        currentPet.setName(updatePet.getName());
        currentPet.setAge(updatePet.getAge());

        // 保存到数据库
        return petRepository.save(currentPet);
    }

    /**
     * 删除宠物
     *
     * @param petId 要删除的宠物 ID
     * @throws ResponseStatusException 如果宠物不存在，抛出 404 错误
     */
    public void deletePet(Long petId) {
        // 先查找宠物（确保它存在）
        Pet petToDelete = findPetById(petId);

        // 从数据库删除
        petRepository.delete(petToDelete);
    }

    // ============ 私有辅助方法 ============

    /**
     * 验证宠物数据
     *
     * 这是一个私有方法，只在 Service 内部使用
     * 把验证逻辑提取出来，避免代码重复
     *
     * @param name 宠物名称
     * @param age 宠物年龄
     * @throws ResponseStatusException 验证失败时抛出异常
     */
    private void validatePet(String name, int age) {
        // 规则 1：名称不能为空
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,  // HTTP 400 错误
                    "Pet name cannot be null or blank");
        }

        // 规则 2：年龄不能为负数
        if (age < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,  // HTTP 400 错误
                    "Pet age cannot be negative");
        }
    }

    /**
     * 按 ID 查找宠物，统一处理"不存在"的情况
     *
     * 【设计模式：DRY 原则】
     * Don't Repeat Yourself - 不要重复自己
     *
     * 多个方法都需要"查找宠物，找不到就返回 404"的逻辑
     * 提取成私有方法，让代码更简洁、更易维护
     *
     * 【JPA 的 Optional】
     * JPA 的 findById 返回 Optional<Pet>，表示"可能有值，也可能没有"
     * Optional.orElseThrow() 的作用：
     * - 如果有值，返回 Pet 对象
     * - 如果没有值，抛出指定的异常
     *
     * @param petId 宠物 ID
     * @return 找到的宠物
     * @throws ResponseStatusException 如果不存在，抛出 404 错误
     */
    private Pet findPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found"));
    }
}
