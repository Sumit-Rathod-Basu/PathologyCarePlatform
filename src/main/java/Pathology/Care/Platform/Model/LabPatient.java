package Pathology.Care.Platform.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lab_patient")
public class LabPatient {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

          private Long labId;  // lowercase 'l'
            private String title;
            private String fullName;
            private String gender;
            private String mobile;
            private String altMobile;
            private String age;
            private String ageType; // Y, M, D
            private String referredBy;
            private String referralLab;
            private String referralHospital;
            private String outsideLab;
            private String height;
            private String weight;


            private String email;
            private String area;
            private String city;
            private String pincode;
            

            private String insuranceNo;
            private String insuranceCompany;
}
