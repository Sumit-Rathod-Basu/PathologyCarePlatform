package Pathology.Care.Platform.Cotroller;

import java.util.List;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import Pathology.Care.Platform.Model.BookingInfo;
import Pathology.Care.Platform.Model.Patient;
import Pathology.Care.Platform.Model.TestEntry;
import Pathology.Care.Platform.Model.TestInfo;
import Pathology.Care.Platform.Service.BookingService;
import Pathology.Care.Platform.Service.LabInfoService;
import Pathology.Care.Platform.Service.Patientservice;
import Pathology.Care.Platform.Service.TestEntryService;
import Pathology.Care.Platform.Service.TestInfoService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/patients")
public class PAtientController {

        @Autowired
        private Patientservice patientservice;

        @Autowired
        private TestInfoService testInfoService;

        @Autowired
        private LabInfoService labInfoService;

        @Autowired
        private BookingService bookingService;

        @Autowired
        private  TestEntryService testEntryService;
@PostMapping("/insert")
public String insertPatient(@ModelAttribute Patient patient) {
    patientservice.savePatient(patient);
    return "success";
}

    @PostMapping("/patient/login")
public String login(
        @ModelAttribute Patient loginRequest,
        HttpSession session,
        Model model) {

    Patient patient = patientservice.login(loginRequest);
    System.out.println("Patient login attempt: " +patient);

    if (patient == null) {
        model.addAttribute("error", "Invalid Email or Password");
        return "patient-login";
    }

    

model.addAttribute("patient", patient);
session.setAttribute("loggedInPatient", patient);
    BookingInfo lastBooking =
    bookingService.getLastBookingByEmail(patient.getEmail());

    model.addAttribute("lastBooking", lastBooking);

    List<TestEntry> testEntries = testEntryService.getAllTestsByEmail(patient.getEmail());
    model.addAttribute("reports", testEntries);
     return "patientsDashbord";  // your file


}
@GetMapping("/GetReport")
public String getReport(Model model, HttpSession session) {
    Patient patient = (Patient) session.getAttribute("loggedInPatient");
    if (patient == null) {
        return "redirect:/patients/patient/login";
    }

    List<TestEntry> testEntries = testEntryService.getAllTestsByEmail1(patient.getEmail());
    System.out.println("Test entries for patient " + patient.getEmail() + ": " + testEntries);
    model.addAttribute("reports", testEntries);
    return "reports"; // your file
}

@GetMapping("/patient/profile")
public String patientProfile(HttpSession session, Model model) {

    Patient patient = (Patient) session.getAttribute("loggedInPatient");
     System.out.println("Logged in patient: " + patient);
    if (patient == null) {
        return "redirect:/patients/patient/login";
    }

    model.addAttribute("patient", patient);
    return "profile"; // Load profile page
}

@GetMapping("/patient/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
}
 
  @GetMapping("/patient/tests")
    public String getAllPublishedTests(Model model) {

        List<TestInfo> tests = testInfoService.getAllPublishedTests();

        model.addAttribute("tests", tests);

        return "patient-tests";  // your HTML page name
    }
  
@GetMapping("/Regesterd/Labs")
public String patientLoginPage(Model model) {
    model.addAttribute("labs", labInfoService.getAllLabs());
    return "registered-labs";
}

}
