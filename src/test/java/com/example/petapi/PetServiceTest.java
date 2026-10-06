package com.example.petapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PetService 的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    public void testCreatePet_Success() {
        // 1. Arrange（准备）
        // 准备输入数据
        PetCreateRequest request = new PetCreateRequest();
        request.setName("测试狗");
        request.setAge(3);

        // 准备模拟的返回数据
        Pet savedPet = new Pet(1L, "测试狗", 3);

        // 模拟 repository.save() 的行为
        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        // 2. Act（执行）
        // 调用要测试的方法
        Pet result = petService.createPet(request);

        // 3. Assert（断言）
        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试狗", result.getName());
        assertEquals(3, result.getAge());

        // 验证 repository.save() 被调用了 1 次
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    public void testGetPetById_Success() {
        // Arrange
        Long petId = 1L;
        Pet mockPet = new Pet(1L, "旺财", 5);

        when(petRepository.findById(petId)).thenReturn(java.util.Optional.of(mockPet));

        // Act
        Pet result = petService.getPetById(petId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("旺财", result.getName());
        assertEquals(5, result.getAge());

        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    public void testGetAllPets_Success() {
        // Arrange
        java.util.List<Pet> mockPets = java.util.Arrays.asList(
            new Pet(1L, "旺财", 3),
            new Pet(2L, "小白", 2)
        );

        // 创建分页对象
        org.springframework.data.domain.Page<Pet> mockPage =
            new org.springframework.data.domain.PageImpl<>(mockPets);

        when(petRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(mockPage);

        // Act
        org.springframework.data.domain.Page<Pet> result = petService.getPets(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("旺财", result.getContent().get(0).getName());
        assertEquals("小白", result.getContent().get(1).getName());

        verify(petRepository, times(1)).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    public void testDeletePet_Success() {
        // Arrange
        Long petId = 1L;
        Pet mockPet = new Pet(1L, "旺财", 3);

        when(petRepository.findById(petId)).thenReturn(java.util.Optional.of(mockPet));
        doNothing().when(petRepository).delete(any(Pet.class));

        // Act
        petService.deletePet(petId);

        // Assert
        verify(petRepository, times(1)).findById(petId);
        verify(petRepository, times(1)).delete(mockPet);
    }

    @Test
    public void testUpdatePet_Success() {
        // Arrange
        Long petId = 1L;
        PetUpdateRequest request = new PetUpdateRequest();
        request.setName("新名字");
        request.setAge(5);

        // 模拟查询到的原始宠物
        Pet existingPet = new Pet(1L, "旧名字", 3);

        // 模拟更新后的宠物
        Pet updatedPet = new Pet(1L, "新名字", 5);

        when(petRepository.findById(petId)).thenReturn(java.util.Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        // Act
        Pet result = petService.updatePet(petId, request);

        // Assert
        assertNotNull(result);
        assertEquals("新名字", result.getName());
        assertEquals(5, result.getAge());

        // 验证调用
        verify(petRepository, times(1)).findById(petId);
        verify(petRepository, times(1)).save(any(Pet.class));
    }
}
