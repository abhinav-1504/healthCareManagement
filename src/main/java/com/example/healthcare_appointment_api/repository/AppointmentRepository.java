package com.example.healthcare_appointment_api.repository;

import com.example.healthcare_appointment_api.model.Appointment;
import com.example.healthcare_appointment_api.model.Doctor;
import com.example.healthcare_appointment_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(User patient);
}