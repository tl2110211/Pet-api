package com.example.petapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Pet 数据访问层（Repository/DAO）
 *
 * 🎉 升级到 JPA 版本！
 *
 * 神奇的变化：
 * 1. 从类变成了接口
 * 2. 不需要写任何实现代码
 * 3. Spring Data JPA 自动提供所有方法
 *
 * JpaRepository<Pet, Long> 的含义：
 * - Pet：实体类型
 * - Long：主键类型
 *
 * 自动提供的方法（完全不用写代码）：
 * - List<Pet> findAll()：查询所有宠物
 * - Optional<Pet> findById(Long id)：根据 ID 查询
 * - Pet save(Pet pet)：保存或更新
 * - void delete(Pet pet)：删除
 * - long count()：统计数量
 *
 * 对比原来的代码：
 * 原来：需要手写 List、遍历查找、手动生成 ID（115 行代码）
 * 现在：只需要声明接口（0 行实现代码）
 *
 * 数据存储：
 * 原来：内存 List，重启就没了
 * 现在：H2 数据库文件，永久保存
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // 就这样！不需要写任何代码！
    // Spring Data JPA 会自动实现上面列出的所有方法

    // 如果以后需要自定义查询，可以这样写（现在不需要）：
    // List<Pet> findByName(String name);  // 根据名字查询
    // List<Pet> findByAgeGreaterThan(int age);  // 查询年龄大于指定值的宠物
    // 只需要按照命名规则写方法名，JPA 会自动生成 SQL！
}
