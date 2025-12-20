package Pathology.Care.Platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDate;

@Service
public class ReportEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReportReadyMail(
            String toEmail,
            String patientName,
            Long patientId
    ) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("🧪 Your Lab Test Report is Ready");

        String loginUrl = "http://localhost:9191/patient/login";

        String htmlContent = """
            <div style="font-family: Arial, sans-serif; padding:20px;">
                <h2 style="color:#0d6efd;">RR Diagnostic Lab</h2>
                <p>Dear <b>%s</b>,</p>

                <p>Your lab test report has been successfully generated.</p>

                <table style="border-collapse:collapse;">
                    <tr>
                        <td><b>Patient ID</b></td>
                        <td>: %d</td>
                    </tr>
                    <tr>
                        <td><b>Report Date</b></td>
                        <td>: %s</td>
                    </tr>
                </table>

                <br>

                <a href="%s"
                   style="
                   background:#0d6efd;
                   color:white;
                   padding:12px 20px;
                   text-decoration:none;
                   border-radius:6px;
                   display:inline-block;
                   font-weight:bold;">
                   🔐 Login & Download Report
                </a>

                <br><br>
                <p style="font-size:13px;color:#555;">
                    For security reasons, reports are available only after login.
                </p>

                <p>Regards,<br>
                <b>PCP Care Team</b></p>
            </div>
        """.formatted(
                patientName,
                patientId,
                LocalDate.now(),
                loginUrl
        );

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
