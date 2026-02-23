package Pathology.Care.Platform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lab_media_assets")
public class LabMediaAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labSealUrl;

    private String labSealPublicId;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private String doctorSignatureUrl;

    private String doctorSignaturePublicId;

    // getters and setters
}
