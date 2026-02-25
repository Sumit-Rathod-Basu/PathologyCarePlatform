package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.Model.AdminUser;
import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Service.AdminService;
import Pathology.Care.Platform.Service.LabInfoService;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private AdminService service;

    @Autowired
    private LabInfoService labService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ LOGIN PAGE
    @GetMapping("/")
    public String showLoginPage() {
        return "admin_login";
    }

    // ✅ PROTECTED DASHBOARD
    @GetMapping("/admin")
    public String dashboard(Model model) {

        model.addAttribute("totalPatients", service.getTotalPatients());
        model.addAttribute("totalLabs", service.getTotalLabs());
        model.addAttribute("totalAdmins", service.getTotalAdmins());

        model.addAttribute("admins", service.getAllAdmins());
        model.addAttribute("patients", service.getAllPatients());
        model.addAttribute("labs", service.getAllLabs());

        return "admin_dashbord";
    }

    // ✅ CREATE ADMIN (password encoded)
    @PostMapping("/admin/create")
    public String createAdmin(@RequestParam("username") String username,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              Model model) {

        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));

        service.saveAdmin(admin);

        return "redirect:/admin/dashboard/admin";
    }

    // ✅ UPDATE ADMIN
    @PostMapping("/admin/update")
    public String updateAdmin(@ModelAttribute AdminUser admin) {
        service.updateAdmin(admin);
        return "redirect:/admin/dashboard/admin";
    }

    // ✅ DELETE ADMIN
    @PostMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        service.deleteAdmin(id);
        return "redirect:/admin/dashboard/admin";
    }

    // ✅ VIEW LABS (also protected)
    @GetMapping("/labs")
    public String viewLabs(Model model) {
        model.addAttribute("labs", service.getAllLabs());
        return "admin_labs";
    }

    // ✅ CREATE LAB
    @PostMapping("/create")
    public String createLab(@RequestParam("labName") String labName,
                            @RequestParam("email") String email,
                            @RequestParam("password") String password,
                            @RequestParam("phone") String phone,
                            @RequestParam("address") String address,
                            @RequestParam("city") String city,
                            @RequestParam("pincode") String pincode,
                            @RequestParam("registrationNumber") String registrationNumber) {

        LabInfo lab = new LabInfo();
        lab.setLabName(labName);
        lab.setEmail(email);
        lab.setPassword(password); // You can encode later if needed
        lab.setPhone(phone);
        lab.setAddress(address);
        lab.setCity(city);
        lab.setPincode(pincode);
        lab.setRegistrationNumber(registrationNumber);

        labService.saveLab(lab);

        return "redirect:/admin/dashboard/labs";
    }
}