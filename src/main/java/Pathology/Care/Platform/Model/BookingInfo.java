package Pathology.Care.Platform.Model;


import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;  
    private String patientEmail;
    private String patientName;
    private Double price;
    private Long testId;
    private String testName;
    private Long labId;
    private String labName;
    private String bookingDate;   // YYYY-MM-DD
    private String status = "PENDING";  // PENDING / CONFIRMED / COMPLETED
}
