package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.TestEntry;
import Pathology.Care.Platform.Service.ReportEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/report")
public class ReportNotificationController {

    @Autowired
    private ReportEmailService reportEmailService;
    @Autowired
    private Pathology.Care.Platform.Service.TestEntryService testEntryService;

    /**
     * Send only report ready email
     * @throws Exception 
     * @throws NumberFormatException 
     */
    @GetMapping("/sendEmail/{id}")
public String sendReportMail(@PathVariable Long id) throws NumberFormatException, Exception {

    // 1. Fetch test entry / patient details
    TestEntry entry = testEntryService.getTest(id);

    // 2. Send mail
    reportEmailService.sendReportReadyMail(
            entry.getPatientEmail(),
            entry.getPatientName(),
            Long.parseLong(entry.getPatientId())
    );

    // 3. Redirect back with success
    return "success";
}

}
