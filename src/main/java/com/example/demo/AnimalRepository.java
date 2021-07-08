package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
//    @Query(value = "UPDATE animal SET height = ?1, weight = ?2, species = ?3, gender = ?4, ownerName = ?5, petName = ?6, WHERE animal.id = ?7", nativeQuery = true)
//    public void updateById(double height, double weight, String species, String gender, String ownerName, String petName, long id);

    public List<Animal> findAllByOrderByIdAsc();

    @Query(value = "SELECT a.* FROM animals a WHERE a.owner_name LIKE %?1% OR a.pet_name LIKE %?1%", nativeQuery = true)
    public List<Animal> findByOwnerNameOrPetName(String name);
}