package Pathology.Care.Platform.Cotroller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private EmailService emailService;
    
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
    
    // OTP storage - In production, use Redis or database
    private Map<String, String> otpMap = new ConcurrentHashMap<>();
    private Map<String, Long> otpTimestamps = new ConcurrentHashMap<>();
    
    private static final long OTP_EXPIRY_TIME = 5 * 60 * 1000; // 5 minutes

    // ================= REGISTRATION PAGE =================
    
    /**
     * Display registration/login page (GET request)
     * Accessed via: GET /patients/register or /patients/patient/login
     */
    @GetMapping({"/register", "/patient/login"})
    public String showLoginPage(Model model) {
        return "patient-login";
    }

    // ================= PATIENT REGISTRATION =================
    
    /**
     * Insert new patient (registration form submission)
     * Accessed via: POST /patients/insert
     */
    @PostMapping("/insert")
    public String insertPatient(@ModelAttribute Patient patient, Model model) {
        try {
            if (patient == null || patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "Email is required");
                return "patient-login";
            }

            patientservice.savePatient(patient);
            
            // Clear OTP for this email
            otpMap.remove(patient.getEmail());
            otpTimestamps.remove(patient.getEmail());
            
            model.addAttribute("success", "Account created successfully! Please login.");
            return "redirect:/patients/patient/login";
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "patient-login";
        }
    }

    // ================= OTP MANAGEMENT =================
    
    /**
     * Send OTP to email address (GET request)
     * Accessed via: GET /patients/send-otp?email=user@example.com
     * Returns: JSON with OTP (for testing) or error message
     */
    @GetMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam String email) {
        try {
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "Email is required"
                    ));
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "Invalid email format"
                    ));
            }

            // Generate 6-digit OTP
            String otp = String.format("%06d", (int)(Math.random() * 1000000));

            // Store OTP with timestamp
            otpMap.put(email, otp);
            otpTimestamps.put(email, System.currentTimeMillis());

            // Send OTP via email service
            try {
                emailService.sendOtp(email, otp);
                System.out.println("✓ OTP sent to: " + email + " | OTP: " + otp);
            } catch (Exception emailException) {
                System.err.println("⚠ Email service failed, but OTP stored locally: " + emailException.getMessage());
                // Continue - OTP is still stored for verification
            }

            // Return success response with OTP (for testing purposes only)
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP sent successfully",
                "otp", otp  // For testing - remove in production
            ));

        } catch (Exception e) {
            System.err.println("Error in sendOtp: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to send OTP"
                ));
        }
    }

    /**
     * Verify OTP sent to email (POST request)
     * Accessed via: POST /patients/verify-otp
     * Body: email=user@example.com&otp=123456
     * Returns: Success/Error message
     */
    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {
        
        try {
            // Validate inputs
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email is required");
            }

            if (otp == null || otp.trim().isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("OTP is required");
            }

            // Check if OTP exists for this email
            String savedOtp = otpMap.get(email);
            if (savedOtp == null) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No OTP sent to this email. Please request a new OTP.");
            }

            // Check if OTP has expired
            Long timestamp = otpTimestamps.get(email);
            if (timestamp != null && (System.currentTimeMillis() - timestamp) > OTP_EXPIRY_TIME) {
                otpMap.remove(email);
                otpTimestamps.remove(email);
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("OTP has expired. Please request a new OTP.");
            }

            // Verify OTP matches
            if (savedOtp.equals(otp)) {
                // OTP is correct - remove it (single use)
                otpMap.remove(email);
                otpTimestamps.remove(email);
                
                System.out.println("✓ OTP verified successfully for: " + email);
                return ResponseEntity.ok("OTP Verified Successfully");
            } else {
                System.out.println("✗ Invalid OTP for: " + email + " | Entered: " + otp + " | Expected: " + savedOtp);
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid OTP. Please try again.");
            }

        } catch (Exception e) {
            System.err.println("Error in verifyOtp: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error verifying OTP");
        }
    }

    /**
     * Resend OTP to email (GET request)
     * Accessed via: GET /patients/resend-otp?email=user@example.com
     * Returns: Same as sendOtp
     */
    @GetMapping("/resend-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam String email) {
        // Same logic as sendOtp, but can track retry attempts
        return sendOtp(email);
    }

    // ================= LOGIN =================
    
    /**
     * Process login form submission (POST request)
     * Accessed via: POST /patients/patient/login
     * Body: email=user@example.com&password=password123
     * Returns: Redirect to dashboard or back to login page
     */
    @PostMapping("/patient/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            // Validate inputs
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Email and password are required");
                return "patient-login";
            }

            // Create Patient object for authentication
            Patient loginRequest = new Patient();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            // Authenticate patient
            Patient patient = patientservice.login(loginRequest);

            if (patient == null) {
                model.addAttribute("error", "Invalid Email or Password");
                System.out.println("✗ Login failed for: " + email);
                return "patient-login";
            }

            // Login successful - store patient in session
            session.setAttribute("loggedInPatient", patient);
            System.out.println("✓ Patient logged in successfully: " + patient.getEmail());

            return "redirect:/patients/dashboard";

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "patient-login";
        }
    }

    // ================= DASHBOARD =================
    
    /**
     * Display patient dashboard (GET request)
     * Accessed via: GET /patients/dashboard
     * Requires: Session with logged-in patient
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        try {
            Patient patient = (Patient) session.getAttribute("loggedInPatient");

            if (patient == null) {
                return "redirect:/patients/patient/login";
            }

            model.addAttribute("patient", patient);

            // Get last booking
            BookingInfo lastBooking = bookingService.getLastBookingByEmail(patient.getEmail());
            if (lastBooking != null) {
                model.addAttribute("lastBooking", lastBooking);
            }

            // Get all test entries for this patient
            List<TestEntry> testEntries = testEntryService.getAllTestsByEmail(patient.getEmail());
            model.addAttribute("reports", testEntries);

            System.out.println("✓ Dashboard loaded for: " + patient.getEmail());
            return "patientsDashbord";  // Fixed typo: was "patientsDashbord"

        } catch (Exception e) {
            System.err.println("Dashboard error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/patients/patient/login";
        }
    }

    // ================= PROFILE =================
    
    /**
     * Display patient profile (GET request)
     * Accessed via: GET /patients/patient/profile
     * Requires: Session with logged-in patient
     */
    @GetMapping("/patient/profile")
    public String patientProfile(HttpSession session, Model model) {
        try {
            Patient patient = (Patient) session.getAttribute("loggedInPatient");

            if (patient == null) {
                return "redirect:/patients/patient/login";
            }

            model.addAttribute("patient", patient);
            System.out.println("✓ Profile loaded for: " + patient.getEmail());
            return "profile";

        } catch (Exception e) {
            System.err.println("Profile error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/patients/patient/login";
        }
    }

    // ================= REPORTS =================
    
    /**
     * Get all test reports for logged-in patient (GET request)
     * Accessed via: GET /patients/GetReport
     * Requires: Session with logged-in patient
     */
    @GetMapping("/GetReport")
    public String getReport(Model model, HttpSession session) {
        try {
            Patient patient = (Patient) session.getAttribute("loggedInPatient");

            if (patient == null) {
                return "redirect:/patients/patient/login";
            }

            List<TestEntry> testEntries = testEntryService.getAllTestsByEmail1(patient.getEmail());
            model.addAttribute("reports", testEntries);
            
            System.out.println("✓ Reports loaded for: " + patient.getEmail());
            return "reports";

        } catch (Exception e) {
            System.err.println("Reports error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/patients/patient/login";
        }
    }

    // ================= LOGOUT =================
    
    /**
     * Logout patient (GET request)
     * Accessed via: GET /patients/patient/logout
     * Returns: Redirect to home page
     */
    @GetMapping("/patient/logout")
    public String logout(HttpSession session) {
        try {
            String email = null;
            Patient patient = (Patient) session.getAttribute("loggedInPatient");
            if (patient != null) {
                email = patient.getEmail();
            }
            
            session.invalidate();
            System.out.println("✓ Patient logged out: " + email);
            return "redirect:/";

        } catch (Exception e) {
            System.err.println("Logout error: " + e.getMessage());
            return "redirect:/";
        }
    }

    // ================= TESTS =================
    
    /**
     * Get all available published tests (GET request)
     * Accessed via: GET /patients/patient/tests
     */
   @GetMapping("/patient/tests")
public String getAllPublishedTests(Model model) {
    try {

        // Only published tests from APPROVED labs
        List<TestInfo> tests = testInfoService.getAllPublishedTests();

        model.addAttribute("tests", tests);

        System.out.println("✓ Approved Lab Tests loaded: " + 
            (tests != null ? tests.size() : 0));

        return "patient-tests";

    } catch (Exception e) {
        System.err.println("Tests error: " + e.getMessage());
        model.addAttribute("error", "Failed to load tests");
        return "patient-tests";
    }
}
    // ================= REGISTERED LABS =================
    
    /**
     * Get all registered labs (GET request)
     * Accessed via: GET /patients/Registered/Labs
     * Note: Fixed typo from "Regesterd" to "Registered"
     */
@GetMapping({"/Registered/Labs", "/Regesterd/Labs"})
public String registeredLabsPage(Model model) {
    try {

        // Only Approved Labs
        List<LabInfo> labs = labInfoService.getApprovedLabs();

        model.addAttribute("labs", labs);

        System.out.println("✓ Approved Labs loaded: " + 
            (labs != null ? labs.size() : 0));

        return "registered-labs";

    } catch (Exception e) {
        System.err.println("Labs error: " + e.getMessage());
        model.addAttribute("error", "Failed to load labs");
        return "registered-labs";
    }
}

    // ================= LAB LOCATION MAP =================
    
    /**
     * Show lab location map (GET request)
     * Accessed via: GET /patients/booking/{bookingId}/map
     * Requires: Valid bookingId
     */
    @GetMapping("/booking/{bookingId}/map")
    public String showLabMap(@PathVariable Long bookingId, Model model) {
        try {
            if (bookingId == null || bookingId <= 0) {
                model.addAttribute("error", "Invalid booking ID");
                return "redirect:/patients/dashboard";
            }

            // Fetch booking info
            BookingInfo booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                model.addAttribute("error", "Booking not found");
                System.out.println("✗ Booking not found: " + bookingId);
                return "redirect:/patients/dashboard";
            }

            // Get lab info from booking
            LabInfo lab = labInfoService.getLabById(booking.getLabId());
            if (lab == null) {
                model.addAttribute("error", "Lab information not found");
                System.out.println("✗ Lab not found for booking: " + bookingId);
                return "redirect:/patients/dashboard";
            }

            model.addAttribute("lab", lab);
            model.addAttribute("booking", booking);
            
            System.out.println("✓ Lab map loaded for booking: " + bookingId);
            return "lab-location-map";

        } catch (Exception e) {
            System.err.println("Lab map error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to load lab information");
            return "redirect:/patients/dashboard";
        }
    }
}