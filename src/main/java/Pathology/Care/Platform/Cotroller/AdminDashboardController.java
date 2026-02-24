package Pathology.Care.Platform.Cotroller;


import org.springframework.beans.factory.annotation.Autowired;
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
    private  AdminService service;

    @Autowired
    private LabInfoService labService;

    public AdminDashboardController(AdminService service) {
        this.service = service;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("totalPatients", service.getTotalPatients());
        model.addAttribute("totalLabs", service.getTotalLabs());
        model.addAttribute("totalAdmins", service.getTotalAdmins());

        model.addAttribute("admins", service.getAllAdmins());
        model.addAttribute("patients", service.getAllPatients());
       // model.addAttribute("labs", service.getAllLabs());

        return "admin_dashbord";
    }

    @PostMapping("/admin/create")
    public String createAdmin(@RequestParam("username") String username,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              Model model) {

        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password); // Ideally, hash the password before saving
       

        service.saveAdmin(admin);

        model.addAttribute("success", "Admin created successfully!");
        return "redirect:/admin/dashboard/admin"; // redirect to admin dashboard page
    }

    @PostMapping("/admin/update")
    public String updateAdmin(@ModelAttribute AdminUser admin) {
        service.updateAdmin(admin);
        return "redirect:/admin/dashboard/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        service.deleteAdmin(id);
        return "redirect:/admin/dashboard/admin";
    }
    
    @GetMapping("/labs")
    public String viewLabs(Model model) {
        model.addAttribute("labs", service.getAllLabs());
        return "admin_labs";
    }

     // Handle lab creation from form
    @PostMapping("/create")
    public String createLab(@RequestParam("labName") String labName,
                            @RequestParam("email") String email,
                            @RequestParam("password") String password,
                            @RequestParam("phone") String phone,
                            @RequestParam("address") String address,
                            @RequestParam("city") String city,
                            @RequestParam("pincode") String pincode,
                            @RequestParam("registrationNumber") String registrationNumber,
                            Model model) {

        LabInfo lab = new LabInfo();
        lab.setLabName(labName);
        lab.setEmail(email);
        lab.setPassword(password); // Optionally, hash password before saving
        lab.setPhone(phone);
        lab.setAddress(address);
        lab.setCity(city);
        lab.setPincode(pincode);
        lab.setRegistrationNumber(registrationNumber);

        labService.saveLab(lab);

        model.addAttribute("success", "Lab registered successfully!");
        return "redirect:/admin/dashboard/labs";
    }

}

