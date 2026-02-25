package Pathology.Care.Platform.Cotroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.Model.*;
import Pathology.Care.Platform.Service.*;

import jakarta.servlet.http.HttpSession;

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
    private TestEntryService testEntryService;

    // ================= REGISTER =================
    @PostMapping("/insert")
    public String insertPatient(@ModelAttribute Patient patient) {
        patientservice.savePatient(patient);
        return "redirect:/patients/patient/login";
    }

    // ================= LOGIN =================
    @PostMapping("/patient/login")
    public String login(@ModelAttribute Patient loginRequest,
                        HttpSession session,
                        Model model) {

        Patient patient = patientservice.login(loginRequest);

        if (patient == null) {
            model.addAttribute("error", "Invalid Email or Password");
            return "patient-login";
        }

        session.setAttribute("loggedInPatient", patient);

        // ✅ Redirect instead of returning view directly
        return "redirect:/patients/dashboard";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Patient patient = (Patient) session.getAttribute("loggedInPatient");

        if (patient == null) {
            return "redirect:/patients/patient/login";
        }

        model.addAttribute("patient", patient);

        BookingInfo lastBooking =
                bookingService.getLastBookingByEmail(patient.getEmail());
        model.addAttribute("lastBooking", lastBooking);

        List<TestEntry> testEntries =
                testEntryService.getAllTestsByEmail(patient.getEmail());
        model.addAttribute("reports", testEntries);

        return "patientsDashbord";
    }

    // ================= PROFILE =================
    @GetMapping("/patient/profile")
    public String patientProfile(HttpSession session, Model model) {

        Patient patient = (Patient) session.getAttribute("loggedInPatient");

        if (patient == null) {
            return "redirect:/patients/patient/login";
        }

        model.addAttribute("patient", patient);
        return "profile";
    }

    // ================= REPORTS =================
    @GetMapping("/GetReport")
    public String getReport(Model model, HttpSession session) {

        Patient patient = (Patient) session.getAttribute("loggedInPatient");

        if (patient == null) {
            return "redirect:/patients/patient/login";
        }

        List<TestEntry> testEntries =
                testEntryService.getAllTestsByEmail1(patient.getEmail());

        model.addAttribute("reports", testEntries);
        return "reports";
    }

    // ================= LOGOUT =================
    @GetMapping("/patient/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================= VIEW TESTS =================
    @GetMapping("/patient/tests")
    public String getAllPublishedTests(Model model) {

        List<TestInfo> tests = testInfoService.getAllPublishedTests();
        model.addAttribute("tests", tests);
            
        return "patient-tests";
    }

    // ================= REGISTERED LABS =================
    @GetMapping("/Regesterd/Labs")
    public String patientLoginPage(Model model) {

        model.addAttribute("labs", labInfoService.getAllLabs());
        return "registered-labs";
    }

@GetMapping("/booking/{bookingId}/map")
public String showLabMap(@PathVariable Long bookingId, Model model) {
    // Fetch booking info
    BookingInfo booking = bookingService.getBookingById(bookingId);
    if (booking == null) {
        return "redirect:/patients/patient/dashboard";
    }

    // Get lab info from booking
    LabInfo lab = labInfoService.getLabById(booking.getLabId());
    if (lab == null) {
        return "redirect:/patients/patient/dashboard";
    }

    model.addAttribute("lab", lab); // send lab info to view
    return "lab-location-map"; // Thymeleaf page
}
}