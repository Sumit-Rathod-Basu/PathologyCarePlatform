package Pathology.Care.Platform.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lab_info")
public class LabInfo {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String labName;

    @Column(unique = true)
    private String email;
    
    private String password;

    private String phone;

    private String address;

    private String city;

    private String pincode;

    private String registrationNumber; 
    
    private String status = "PENDING";
}
