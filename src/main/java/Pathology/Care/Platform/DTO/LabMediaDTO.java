package Pathology.Care.Platform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabMediaDTO {

    private Long id;
    private String labSealUrl;
    private String labSealPublicId;
    private String doctorName;
    private String doctorSignatureUrl;
    private String doctorSignaturePublicId;

    // getters and setters
}
