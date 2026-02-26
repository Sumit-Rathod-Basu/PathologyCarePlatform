package Pathology.Care.Platform.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "doctors")
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates default constructor
@AllArgsConstructor // Generates all-args constructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String specialization;

    @Column
    private String phone;
}