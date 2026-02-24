package Pathology.Care.Platform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String reply;

    private LocalDateTime createdAt;
    private LocalDateTime repliedAt;

    private boolean replied = false;

    public ContactMessage() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    // ...
}