package Pathology.Care.Platform.Repository;


import Pathology.Care.Platform.Model.TestInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<TestInfo, Long> {

    List<TestInfo> findByLabId(Long labId);

    List<TestInfo> findByPublish(String string);
    
}