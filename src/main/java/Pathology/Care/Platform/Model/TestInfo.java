package Pathology.Care.Platform.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lab_tests")
public class TestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long labId;  

    private String labName;

    private String publish = "PUBLISH";

    private String testName;

    private String category;  // Blood, Urine, Diabetes, Thyroid etc.

    private String description;

    private Double price;

    private String sampleType;  // Blood / Urine / Swab

    private String reportTime;  // 24 hrs, 48 hrs, Same Day

    private String status; // Active / Inactive
}