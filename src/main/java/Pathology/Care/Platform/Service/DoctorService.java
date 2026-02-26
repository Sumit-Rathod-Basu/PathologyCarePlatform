package Pathology.Care.Platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Pathology.Care.Platform.Model.Doctor;
import Pathology.Care.Platform.Repository.DoctorRepository;

import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Register doctor
    public Doctor registerDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Login doctor
    public Doctor loginDoctor(String email, String password) {
        Optional<Doctor> optional = doctorRepository.findByEmail(email);
        if(optional.isPresent() && optional.get().getPassword().equals(password)) {
            return optional.get();
        }
        return null;
    }

    // Get doctor by id
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    // Update doctor profile
    public Doctor updateDoctorProfile(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
}