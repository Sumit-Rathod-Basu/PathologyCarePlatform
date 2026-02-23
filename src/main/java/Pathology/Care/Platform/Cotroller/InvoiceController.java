package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.ResultSummary;
import Pathology.Care.Platform.Model.TestEntry;
import Pathology.Care.Platform.Service.Barcodeservice;
import Pathology.Care.Platform.Service.QRCodeService;
import Pathology.Care.Platform.Service.ResultSummarysave;
import Pathology.Care.Platform.Service.TestEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private TestEntryService testEntryService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private Barcodeservice barcodeService;

    @Autowired
   private ResultSummarysave resultSummarysave;

    @GetMapping("/{patientId}")
    public String generateInvoice(@PathVariable String patientId, Model model) throws Exception {
        // Fetch all test entries for this patient
        Long idLong=Long.parseLong(patientId);
        TestEntry test=testEntryService.getTest(idLong);
        System.out.println("all data is :"+test);
         List<TestEntry> tests = testEntryService.getTestsByPatientId(test.getPatientId());
       
        //System.out.println("Patient ID: " + id);
    
   /*     ResultSummary summary = resultSummarysave.getByTestEntryId(Long.parseLong(test.getPatientId()));
         System.out.println("Summary: " + summary);
         List<String> summaryPoints = new ArrayList<>();
    if (summary != null && summary.getSummaryText() != null) {
        // Split by full stop
        String[] sentences = summary.getSummaryText().split("\\.");
        for (String s : sentences) {
            if (!s.trim().isEmpty()) {
                summaryPoints.add(s.trim());
            }
        }
    } */
    List<ResultSummary> summaries =
        resultSummarysave.getByTestEntryId(Long.parseLong(test.getPatientId()));

System.out.println("Summaries: " + summaries);

List<String> summaryPoints = new ArrayList<>();

for (ResultSummary summary : summaries) {
    if (summary != null && summary.getSummaryText() != null) {

        String[] sentences = summary.getSummaryText().split("\\.");

        for (String s : sentences) {
            if (!s.trim().isEmpty()) {
                summaryPoints.add(s.trim());
            }
        }
    }
}

        // Generate QR code text (can be patient info + invoice ID)
        String qrText = "Patient ID: " + patientId + ", Total Tests: " + tests.size();
        String barcodeText = "Patient ID: " + patientId + ", Total Tests: " + tests.size();
        String qrCode = qrCodeService.generateQRCodeBase64(qrText, 150, 150);
        String barcode = barcodeService.generateBarcodeBase64(barcodeText);

        model.addAttribute("tests", tests);
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("barcode", barcode);
        model.addAttribute("patientId", patientId);
        model.addAttribute("summaryPoints", summaryPoints);
        model.addAttribute("patientName", tests.get(0).getPatientName());
        model.addAttribute("patientMobile", tests.get(0).getPatientMobile());
        model.addAttribute("patientEmail", tests.get(0).getPatientEmail());

        return "invoiceprint";
    }
    
}