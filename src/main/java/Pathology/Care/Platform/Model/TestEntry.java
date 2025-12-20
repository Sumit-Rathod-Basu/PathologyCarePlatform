package Pathology.Care.Platform.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_entries")
public class TestEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int srNo;
    private String testField;
    private String testValue;
    private String unit;
    private String testRange;
    private String patientId;
    private String patientName;
    private String patientMobile;
    private String patientEmail;
    private String summary;
    private Long labId;
}
