package Pathology.Care.Platform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Pathology.Care.Platform.Model.Doctor;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
}