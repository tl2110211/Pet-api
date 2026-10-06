package com.example.petapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // 就这样！一行代码都不用写！
    // Spring Data JPA 自动提供所有方法
}
