package Pathology.Care.Platform.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "lab_settings")
public class LabSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String labName;
    private String doctorName;

    private String logoUrl;
    private String sealUrl;
    private String signatureUrl;
    private String letterheadUrl;

    // Getters and Setters

    public Long getId() { return id; }

    public String getLabName() { return labName; }
    public void setLabName(String labName) { this.labName = labName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getSealUrl() { return sealUrl; }
    public void setSealUrl(String sealUrl) { this.sealUrl = sealUrl; }

    public String getSignatureUrl() { return signatureUrl; }
    public void setSignatureUrl(String signatureUrl) { this.signatureUrl = signatureUrl; }

    public String getLetterheadUrl() { return letterheadUrl; }
    public void setLetterheadUrl(String letterheadUrl) { this.letterheadUrl = letterheadUrl; }
}
