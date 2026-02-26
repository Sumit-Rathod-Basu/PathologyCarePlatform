package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Pathology.Care.Platform.Model.Doctor;
import Pathology.Care.Platform.Service.DoctorService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    // Show doctor admin page with search box
    @GetMapping("/admin")
    public String doctorAdmin() {
        return "doctor_admin"; // Thymeleaf file name
    }

    // Handle search and redirect to /invoice/{id}
    @GetMapping("/search")
    public String searchReport(@RequestParam("id") Long reportId) {
        // Redirect to the invoice page with the entered ID
        return "redirect:/invoice/" + reportId;
    }


    @Autowired
    private DoctorService doctorService;

    // ================= REGISTER =================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor-register";
    }

    @PostMapping("/register")
    public String registerDoctor(@ModelAttribute Doctor doctor, Model model) {
        doctorService.registerDoctor(doctor);
        return "redirect:/doctor/login";
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String showLoginForm() {
        return "doctor-login";
    }

    @PostMapping("/login")
    public String loginDoctor(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        Doctor doctor = doctorService.loginDoctor(email, password);
        if (doctor == null) {
            model.addAttribute("error", "Invalid Email or Password");
            return "doctor-login";
        }
        session.setAttribute("loggedInDoctor", doctor);
        return "redirect:/doctor/dashboard";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String doctorDashboard(HttpSession session, Model model) {
        Doctor doctor = (Doctor) session.getAttribute("loggedInDoctor");
        if (doctor == null) {
            return "redirect:/doctor/login";
        }
        model.addAttribute("doctor", doctor);
        return "doctor-dashboard";
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String doctorProfile(HttpSession session, Model model) {
        Doctor doctor = (Doctor) session.getAttribute("loggedInDoctor");
        if (doctor == null) {
            return "redirect:/doctor/login";
        }
        model.addAttribute("doctor", doctor);
        return "doctor-profile";
    }

    @PostMapping("/profile/update")
    public String updateDoctorProfile(@ModelAttribute Doctor doctor, HttpSession session) {
        Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
        if (loggedInDoctor == null) return "redirect:/doctor/login";

        doctor.setId(loggedInDoctor.getId()); // ensure correct ID
        doctorService.updateDoctorProfile(doctor);
        session.setAttribute("loggedInDoctor", doctor);
        return "redirect:/doctor/dashboard";
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public String logoutDoctor(HttpSession session) {
        session.invalidate();
        return "redirect:/doctor/login";
    }
}