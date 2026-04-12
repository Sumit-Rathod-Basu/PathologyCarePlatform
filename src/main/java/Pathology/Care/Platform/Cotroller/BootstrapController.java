package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Pathology.Care.Platform.Model.AdminUser;
import Pathology.Care.Platform.Repository.AdminRepo;

@RestController
public class BootstrapController {

@Autowired
private AdminRepo adminRepository;

@Autowired
private PasswordEncoder passwordEncoder;
@GetMapping("/new")
@ResponseBody
public String createFirstAdmin() {

    // If admin already exists, stop
    if (adminRepository.count() > 0) {
        return "Admin already exists!";
    }

    AdminUser admin = new AdminUser();
    admin.setUsername("superadmin");
    admin.setEmail("admin@rrlab.com");

    // 🔐 encoded password
    admin.setPassword(passwordEncoder.encode("admin123"));

    adminRepository.save(admin);

    return "First Admin Created Successfully!";
}
@GetMapping("/delete-admin")
@ResponseBody
public String deleteAdmin() {

    if (adminRepository.count() == 0) {
        return "No admin found!";
    }

    adminRepository.deleteAll(); // delete all admins

    return "Admin deleted successfully!";
}
}
