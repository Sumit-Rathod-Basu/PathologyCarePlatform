package Pathology.Care.Platform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Pathology.Care.Platform.Model.Patient;

@Repository
public interface patientrepo  extends JpaRepository<Patient, Long>
{

  Patient findByEmail(String email);
         
}
