package Pathology.Care.Platform.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import Pathology.Care.Platform.Model.LabSettings;

public interface LabSettingsRepository extends JpaRepository<LabSettings, Long> {
    Optional<LabSettings> findByLabName(String labName);
}