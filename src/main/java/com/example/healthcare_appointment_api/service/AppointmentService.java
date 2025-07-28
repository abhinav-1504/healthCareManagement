package com.example.healthcare_appointment_api.service;

import com.example.healthcare_appointment_api.model.Appointment;
import com.example.healthcare_appointment_api.model.Doctor;
import com.example.healthcare_appointment_api.model.User;
import com.example.healthcare_appointment_api.repository.AppointmentRepository;
import com.example.healthcare_appointment_api.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public Appointment bookAppointment(Appointment appointment) {
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new RuntimeException("Doctor ID is required");
        }
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        if (appointmentTime == null) {
            throw new RuntimeException("Appointment time is required");
        }
        if (appointmentRepository.existsByDoctorAndAppointmentTime(doctor, appointmentTime)) {
            throw new RuntimeException("Doctor is not available at this time");
        }
        appointment.setDoctor(doctor); // Ensure full Doctor entity is set
        appointment.setPatient(getCurrentUser());
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointments() {
        User user = getCurrentUser();
        if (user.getRoles().contains("ROLE_DOCTOR")) {
            Doctor doctor = doctorRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
            return appointmentRepository.findByDoctor(doctor);
        } else {
            return appointmentRepository.findByPatient(user);
        }
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}