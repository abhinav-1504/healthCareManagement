package com.example.healthcare_appointment_api.repository;

import com.example.healthcare_appointment_api.model.Doctor;
import com.example.healthcare_appointment_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    List<Doctor> findBySpecialty(String specialty);
}
