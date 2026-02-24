package Pathology.Care.Platform.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String username;
    public String email;
    public String password; // store hashed password in production
    

    // Getters and Setters
}