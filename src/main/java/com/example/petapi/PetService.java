package com.example.petapi;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Page<Pet> getPets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return petRepository.findAll(pageable);
    }

    public Pet getPetById(Long petId) {
        return findPetById(petId);
    }

    public int countPets() {
        return (int) petRepository.count();
    }

    public Pet createPet(PetCreateRequest newPet) {

        Pet petToCreate = new Pet(null, newPet.getName(), newPet.getAge());

        return petRepository.save(petToCreate);
    }

    public Pet updatePet(Long petId, PetUpdateRequest updatePet) {

        Pet currentPet = findPetById(petId);
        currentPet.setName(updatePet.getName());
        currentPet.setAge(updatePet.getAge());

        return petRepository.save(currentPet);
    }

    public void deletePet(Long petId) {
        Pet petToDelete = findPetById(petId);
        petRepository.delete(petToDelete);
    }

    // 按 ID 查询，不存在时统一返回 404
    private Pet findPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found with id: " + petId));
    }
}
