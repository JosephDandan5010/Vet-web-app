package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query(value = "UPDATE app SET name = ?1, illness = ?2, status = ?3, time = ?4, WHERE app.id = ?5", nativeQuery = true)
    public void updateById( String name, String illness, String status, String time, long id);

    public List<Appointment> findAllByOrderByIdAsc();

    @Query(value = "SELECT app.* FROM appointments app WHERE app.name LIKE %?1% OR app.illness LIKE %?1% OR app.time LIKE %?1%", nativeQuery = true)
    public List<Appointment> findByName(String name);
}
