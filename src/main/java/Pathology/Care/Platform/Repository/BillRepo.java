package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Pathology.Care.Platform.Model.Bill;

@Repository
public interface BillRepo  extends JpaRepository<Bill, Long> {

    List<Bill> findByPatientId(Long patientId);
}
