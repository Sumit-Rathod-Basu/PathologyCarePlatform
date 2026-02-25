package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Pathology.Care.Platform.Model.LabPatient;

public interface LabPatientRepo extends JpaRepository<LabPatient, Long> {

    List<LabPatient> findAllByLabId(long LabId);

    Long countByLabId(Long labId);

    @Query("SELECT p.id FROM LabPatient p WHERE p.labId = :labId")
    List<Long> findAllPatientIdsByLabId(Long labId);
}
