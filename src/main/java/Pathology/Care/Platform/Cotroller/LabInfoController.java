package Pathology.Care.Platform.Cotroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Service.DashboardService;
import Pathology.Care.Platform.Service.EmailService;
import Pathology.Care.Platform.Service.LabInfoService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/lab")

public class LabInfoController {

        @Autowired
    private DashboardService dashboardService;
    @Autowired
    private LabInfoService service;
        @Autowired
    private EmailService emailService;
    

    // Temporary storage of OTPs (email -> OTP)
    private Map<String, String> otpMap = new HashMap<>();

    // ------------------ REGISTER LAB ------------------
    @PostMapping("/register")
    public String register(@ModelAttribute LabInfo lab, Model model) {
        service.registerLab(lab);
        model.addAttribute("msg", "Lab Registered Successfully!");
        return "success";
    }


    @GetMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestParam String email) {
        // Generate 6-digit OTP
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        // Store OTP temporarily
        otpMap.put(email, otp);

        // Send OTP email
        emailService.sendOtp(email, otp);

        // Return OTP for demo/testing (optional in production)
        return Map.of("otp", otp);
    }

    @PostMapping("/verify-otp")
public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
    String savedOtp = otpMap.get(email); // otpMap from /send-otp
    if(savedOtp != null && savedOtp.equals(otp)) {
        otpMap.remove(email); // OTP consumed
        return ResponseEntity.ok("OTP Verified");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }
}

    @PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    HttpSession session,
                    Model model) {

    LabInfo lab = service.login(email, password);

    // ❌ Invalid credentials
    if (lab == null) {
        model.addAttribute("error", "Invalid email or password!");
        return "lab-login";
    }

    // 🔥 Check Lab Status BEFORE login
    if (!"APPROVED".equalsIgnoreCase(lab.getStatus())) {

        System.out.println("Login blocked. Lab not approved. Status: " + lab.getStatus());

        // Do NOT create session
        return "approval-pending";   // approval-pending.html page
    }

    // Fetch analytics data

    session.setAttribute("labId", lab.getId());
    session.setAttribute("labName", lab.getLabName());
    session.setAttribute("lab", lab);

    System.out.println("Login Success! Lab ID: " + lab.getId());
    System.out.println("Lab Name: " + lab.getLabName());

    return "redirect:/lab/dashboard";
}

    // ------------------ DASHBOARD ---------------------
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        LabInfo lab = (LabInfo) session.getAttribute("lab");

        if (lab == null) {
            return "redirect:/lab-login";
        }
     Long totalPatients = dashboardService.getTotalPatients(lab.getId());
    int totalBookings = dashboardService.getTotalBookings(lab.getId()).size();
    int totalTests = dashboardService.getTotalTests(lab.getId()).size();
    Double totalRevenue = dashboardService.getTotalRevenue(lab.getId());

    // Add to model
    model.addAttribute("totalPatients", totalPatients);
    model.addAttribute("totalBookings", totalBookings);
    model.addAttribute("totalTests", totalTests);
    model.addAttribute("totalRevenue", totalRevenue);
    // ✅ If Approved → Create session
        model.addAttribute("lab", lab);
        return "lab-dashboard";
    }

    // ------------------ PROFILE PAGE ------------------
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        LabInfo lab = (LabInfo) session.getAttribute("lab");

        if (lab == null) {
            return "redirect:/lab/login";
        }

        model.addAttribute("lab", lab);
        return "lab-profile";
    }
    // ------------------ EDIT PROFILE ------------------
    @GetMapping("/lab/profile/edit")
    public String editProfile(HttpSession session, Model model) {
        LabInfo lab = (LabInfo) session.getAttribute("lab");

        if (lab == null) {
            return "redirect:/lab/login";
        }

        model.addAttribute("lab", lab);
        return "edit-profile";
    }
    // ------------------ UPDATE PROFILE ------------------
    @PostMapping("/lab/profile/update")
    public String updateProfile(@ModelAttribute LabInfo lab, HttpSession session) {

        LabInfo updated = service.updateLab(lab);
        session.setAttribute("lab", updated);

        return "redirect:/lab/profile";
    }

    // ------------------ LOGOUT ------------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @PostMapping("/delete")
    public String deleteLab(@RequestParam Long id) {
        service.deleteLab(id);
        return "redirect:/admin/dashboard/labs";
    }
    
    @PostMapping("/admin/lab/update-status/{id}")
public String updateLabStatus(@PathVariable Long id,
                              @RequestParam String status) {

    service.updateStatus(id, status);

    LabInfo lab = service.getLabById(id);
if ("APPROVED".equalsIgnoreCase(status)) {
    service.sendApprovalEmail(lab.getEmail(), lab.getLabName());
}
else if ("PENDING".equalsIgnoreCase(status)) {
    service.sendPendingEmail(lab.getEmail(), lab.getLabName());
}

    return "redirect:/admin/dashboard/labs";
}
    
}
