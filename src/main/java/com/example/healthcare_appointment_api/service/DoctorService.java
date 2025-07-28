package com.example.healthcare_appointment_api.service;

import com.example.healthcare_appointment_api.model.Doctor;
import com.example.healthcare_appointment_api.model.User;
import com.example.healthcare_appointment_api.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserService userService;

    public Doctor createDoctorProfile(Doctor doctor) {
        doctor.setUser(getCurrentUser());
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor updateDoctorProfile(Long id, Doctor updatedDoctor) {
        Doctor doctor = getDoctorById(id);
        if (!doctor.getUser().getUsername().equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialty(updatedDoctor.getSpecialty());
        doctor.setContact(updatedDoctor.getContact());
        return doctorRepository.save(doctor);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }
}
