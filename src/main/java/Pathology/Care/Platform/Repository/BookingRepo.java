package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import Pathology.Care.Platform.Model.BookingInfo;

public interface BookingRepo extends JpaRepository<BookingInfo, Long> {
    List<BookingInfo> findByLabName(String labName);
   List<BookingInfo> findByLabId(Long labId);
       // Get latest booking of patient using email
       List<BookingInfo> findBypatientEmail(String email);
    BookingInfo findTopByPatientEmailOrderByIdDesc(String patientEmail);
}
