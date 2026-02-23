package Pathology.Care.Platform.Repository;

import Pathology.Care.Platform.Model.LabMediaAssets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabMediaRepository extends JpaRepository<LabMediaAssets, Long> {
}