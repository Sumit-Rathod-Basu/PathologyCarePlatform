package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Pathology.Care.Platform.Model.LabPatient;

public interface LabPatientRepo extends JpaRepository<LabPatient, Long> {

    List<LabPatient> findAllByLabId(long LabId);

}
