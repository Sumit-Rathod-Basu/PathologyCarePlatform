package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import Pathology.Care.Platform.Model.TestEntry;
@Repository
public interface TestEntryRepo extends JpaRepository<TestEntry, Long> 
{
@Query(value = """
    SELECT * FROM test_entries t
    WHERE t.lab_id = :labId
      AND t.id IN (
          SELECT MAX(id)
          FROM test_entries
          WHERE lab_id = :labId
          GROUP BY patient_id
      )
    ORDER BY t.id DESC
    """, nativeQuery = true)
List<TestEntry> findLatestDistinctPatientsByLabId(@Param("labId") Long labId);

}
