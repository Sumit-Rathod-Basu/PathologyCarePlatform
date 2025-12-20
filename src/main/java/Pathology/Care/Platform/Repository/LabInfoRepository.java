package Pathology.Care.Platform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Pathology.Care.Platform.Model.LabInfo;

public interface LabInfoRepository extends JpaRepository<LabInfo, Long> {

      LabInfo findByEmail(String email);

    LabInfo findByEmailAndPassword(String email, String password);
}
