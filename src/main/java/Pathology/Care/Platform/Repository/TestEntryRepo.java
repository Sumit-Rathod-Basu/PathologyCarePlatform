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

/* @Query(value = """
    SELECT t.*
    FROM test_entries t
    JOIN patients p ON t.patient_id = p.id
    WHERE p.email = :email
      AND t.id IN (
          SELECT MAX(t2.id)
          FROM test_entries t2
          JOIN patients p2 ON t2.patient_id = p2.id
          WHERE p2.email = :email
          GROUP BY t2.patient_id
      )
    ORDER BY t.id DESC
    """, nativeQuery = true)
List<TestEntry> findLatestByPatientEmailGroupedByPatient(
        @Param("email") String email); */

List<TestEntry> findTopByPatientEmailOrderByIdDesc(String patientEmail);


@Query(value = """
    SELECT t.*
    FROM test_entries t
    JOIN patients p ON t.patient_id = p.id
    WHERE p.email = :email
      AND t.id IN (
          SELECT MAX(t2.id)
          FROM test_entries t2
          JOIN patients p2 ON t2.patient_id = p2.id
          WHERE p2.email = :email
          GROUP BY t2.patient_id
      )
    ORDER BY t.id DESC
    """, nativeQuery = true)
List<TestEntry> findLatestByPatientEmailGroupedByPatient(
        @Param("email") String email);



 
}
