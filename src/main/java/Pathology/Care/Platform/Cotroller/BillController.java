package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.Bill;
import Pathology.Care.Platform.Model.TestEntry;
import Pathology.Care.Platform.Repository.BillRepo;
import Pathology.Care.Platform.Service.Barcodeservice;
import Pathology.Care.Platform.Service.BillingService;
import Pathology.Care.Platform.Service.QRCodeService;
import Pathology.Care.Platform.Service.TestEntryService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BillController {

    @Autowired
    private BillRepo billRepository;

    @Autowired
    private BillingService billingService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private Barcodeservice barcodeservice;

    @Autowired
    private  TestEntryService service;



    @Autowired
    private ObjectMapper objectMapper; // Jackson JSON mapper

    @PostMapping("/billing/save1")
public String saveBilling(
        @RequestParam("testsJson") String testsJson,
        @RequestParam("patientId") Long patientId,
        RedirectAttributes redirectAttributes
) {
    try {
        // Parse JSON to List<Bill>
        List<Bill> tests = objectMapper.readValue(testsJson, new TypeReference<List<Bill>>(){});

        // Fetch patient (optional)
       // LabPatient patient = patientRepository.findById(patientId)
         //       .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Calculate and set totalPrice for each Bill
        tests.forEach(test -> {
            double price = test.getPrice() != null ? test.getPrice() : 0.0;
            double discount = test.getDiscount() != null ? test.getDiscount() : 0.0;

            // Total Price = Price - (Price * Discount / 100)
            double totalPrice = price - (price * discount / 100);
            test.setTotalPrice(totalPrice);
            test.setPatientId(patientId);
            // Optionally associate patient
           // test.setPatient(patient);
        });

        // Save all bills
        billRepository.saveAll(tests);

        redirectAttributes.addFlashAttribute("successMessage", "Bill saved successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("errorMessage", "Error saving bill: " + e.getMessage());
    }
    
    return "redirect:/load?patientId=" + patientId; 
}
@GetMapping("/bill/invoice/{id}")
public String generateInvoice(@PathVariable Long id, Model model) throws Exception {
    
    List<Bill> bill = billingService.getPatientById(id);

     String id1=id.toString();
    List<TestEntry> tests =  service.getTestsByPatientId(id1);
     double grandTotal = bill.stream().mapToDouble(Bill::getTotalPrice).sum();
    // QR Code generation
    String qrText = "Name: " + tests.get(0).getPatientName() + ", Total price:" +grandTotal;
    String qrCode = qrCodeService.generateQRCodeBase64(qrText, 150, 150);

    // Barcode generation
    String barcodeText ="Name: " + tests.get(0).getPatientName() + ", Total price:" +bill.get(0).getTotalPrice();
    String barcode = barcodeservice.generateBarcodeBase64(barcodeText);
   
     model.addAttribute("grandTotal", grandTotal);


    model.addAttribute("bill", bill);
    model.addAttribute("qrCode", qrCode);
    model.addAttribute("barcode", barcode);
   model.addAttribute("billDate", LocalDate.now());
        model.addAttribute("patientName", tests.get(0).getPatientName());
        model.addAttribute("patientMobile", tests.get(0).getPatientMobile());
        model.addAttribute("patientEmail", tests.get(0).getPatientEmail());
        model.addAttribute("total",bill.get(0).getTotalPrice());

    return "bill";
}


}
