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
    public AdminUser orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
    

    // Getters and Setters
}