package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository repo;

    public List<Appointment> findAllOrderByIdAsc() {
        return repo.findAllByOrderByIdAsc();
    }

    public void save(Appointment animal) {
        repo.save(animal);
    }

    public Appointment get(long id) {
        return repo.findById(id).get();
    }

    public void updateName(Appointment name) {
        repo.save(name);
    }

    public void updateIllness(Appointment illness) {
        repo.save(illness);
    }

    public void updateStatus(Appointment status) {
        repo.save(status);
    }

    public void updateTime(Appointment time) {
        repo.save(time);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }
}