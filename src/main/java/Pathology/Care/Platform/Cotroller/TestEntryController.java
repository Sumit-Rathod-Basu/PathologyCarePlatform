package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import Pathology.Care.Platform.Model.ResultSummary;
import Pathology.Care.Platform.Model.TestEntry;
import Pathology.Care.Platform.Service.ResultSummarysave;
import Pathology.Care.Platform.Service.TestEntryService;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller 
@RequestMapping("/api/test")
public class TestEntryController {
    
    @Autowired
    private  TestEntryService service;
    
    @Autowired
   private ResultSummarysave resultSummarysave;


    
    @PostMapping("/add")
    public TestEntry addTest(@RequestBody TestEntry testEntry) {
        return service.saveTest(testEntry);
    }

    // Add multiple test entries (list)
    @PostMapping("/addAll")
    @ResponseBody
    public List<TestEntry> addAllTests(@RequestBody List<TestEntry> entries,HttpSession session) {
        //LabPatient patient=labpatientService.getPatientById(entries.get(0).getId());
      //  entries.get(0).setLabId(patient.getLabId());
       Long id = (Long) session.getAttribute("labId");
         entries.forEach(entry -> entry.setLabId(id));
        return service.saveAllTests(entries);
    }
    // access using following URL /api/test/all
    // Get all test entries
@GetMapping("/all")
public String listAllTests(Model model,HttpSession session) {
      
   
   
   // Long id =model.getAttribute("labId")!=null ? (Long) model.getAttribute("labId") : 1L;
   Long id = (Long) session.getAttribute("labId");
    List<TestEntry> testEntries = service.getAllTestsByLabID(id);
    model.addAttribute("testEntries", testEntries);
    

    return "list-test-entries"; // Thymeleaf file
}
    // Get test entry by ID
    @GetMapping("/{id}")
    public TestEntry getTest(@PathVariable Long id) {
        return service.getTest(id);
    }
    // Update test entry
    @PutMapping("/update/{id}")
    public TestEntry updateTest(
            @PathVariable Long id,
            @RequestBody TestEntry updated
    ) {
        return service.updateTest(id, updated);
    }

    // Delete test entry
    @PostMapping("/delete/{id}")
    public String deleteTest(@PathVariable Long id) {
        service.deleteTest(id);
        return "success"; // Return empty response    
    }

    // access using following URL /api/test/view/{id}

     @GetMapping("/view/{patientId}") 
      public String generateInvoice(@PathVariable String patientId, Model model) throws Exception {
        // Fetch all test entries for this patient
        Long idLong=Long.parseLong(patientId);
        TestEntry test=service.getTest(idLong);
        System.out.println("all data is :"+test);
       
        List<TestEntry> tests =  service.getTestsByPatientId(test.getPatientId());
        System.out.println("Tests: " + tests);
        //System.out.println("Patient ID: " + id);
    
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


    /*     TestEntry first = tests.get(0);
    System.out.println("First Test Entry: " + first);
    model.addAttribute("patientId", first.getPatientId());
    model.addAttribute("patientName", first.getPatientName());
    model.addAttribute("patientMobile", first.getPatientMobile());
    model.addAttribute("patientEmail", first.getPatientEmail());
    model.addAttribute("tests", tests);

    return "view-test-entry"; */

        model.addAttribute("tests", tests);
        model.addAttribute("patientId", test.getPatientId());
        model.addAttribute("summaryPoints", summaryPoints);
        model.addAttribute("patientName", tests.get(0).getPatientName());
        model.addAttribute("patientMobile", tests.get(0).getPatientMobile());
        model.addAttribute("patientEmail", tests.get(0).getPatientEmail());
        return "view-test-entry"; 
    }
    
}
