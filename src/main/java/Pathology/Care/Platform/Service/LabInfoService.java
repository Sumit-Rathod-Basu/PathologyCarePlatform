package Pathology.Care.Platform.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Repository.LabInfoRepository;

@Service
public class LabInfoService {

    @Autowired
    private LabInfoRepository repo;

        @Autowired
    private JavaMailSender mailSender;

    // REGISTER LAB
    public LabInfo registerLab(LabInfo lab) {
        return repo.save(lab);
    }

    // LOGIN LAB
    public LabInfo login(String email, String password) {
        return repo.findByEmailAndPassword(email, password);
    }
    // UPDATE LAB
    public LabInfo updateLab(LabInfo lab) {
        return repo.save(lab);
    }


    public void deleteLab(Long id) {
        // TODO Auto-generated method stub
          repo.deleteById(id);
    }

    public LabInfo saveLab(LabInfo lab) {
        // TODO Auto-generated method stub
        return repo.save(lab);
    }

    public LabInfo getLabById(Long labId) {
        // TODO Auto-generated method stub
        return repo.findById(labId).orElse(null);
    }
        public List<LabInfo> getApprovedLabs() {
        return repo.findByStatus("APPROVED");
    }

    public List<LabInfo> getAllLabs() {
        return repo.findAll();
    }

      public void updateStatus(Long id, String status) {
        LabInfo lab = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab not found"));

        lab.setStatus(status);   // Only updating status
        repo.save(lab);
    }


     public void sendApprovalEmail(String toEmail, String labName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Lab Approval Notification");

        message.setText(
                "Dear " + labName + ",\n\n" +
                "Congratulations! 🎉\n\n" +
                "Your laboratory has been APPROVED by the admin.\n" +
                "You can now login and start using the platform.\n\n" +
                "Regards,\nPathology Care Team"
        );

        mailSender.send(message);
    }

     public void sendPendingEmail(String toEmail, String labName) {

    try {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Lab Approval Status - Pending");

        message.setText(
                "Dear " + labName + ",\n\n" +
                "Your lab registration is currently marked as PENDING.\n\n" +
                "Our admin team is reviewing your details.\n" +
                "You will be notified once it is approved.\n\n" +
                "Thank you for your patience.\n\n" +
                "Regards,\n" +
                "Pathology Management Team"
        );

        mailSender.send(message);

        System.out.println("Pending email sent successfully to: " + toEmail);

    } catch (Exception e) {
        System.err.println("Error sending pending email: " + e.getMessage());
        e.printStackTrace();
    }
}
}
