package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnimalService {

    @Autowired
    private AnimalRepository repo;

    public List<Animal> findAllOrderByIdAsc() {
        return repo.findAllByOrderByIdAsc();
    }

    public void save(Animal animal) {
        repo.save(animal);
    }

    public Animal get(long id) {
        return repo.findById(id).get();
    }

    public void updateHeight(Animal height) {
        repo.save(height);
    }

    public void updateWeight(Animal weight) {
        repo.save(weight);
    }

    public void updateGender(Animal gender) {
        repo.save(gender);
    }

    public void updateSpecies(Animal species) {
        repo.save(species);
    }

    public void updateOwnerName(Animal ownerName) {
        repo.save(ownerName);
    }

    public void updatePetName(Animal petName) {
        repo.save(petName);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}