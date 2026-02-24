package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Service.LabInfoService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/lab")
public class LabInfoController {

    @Autowired
    private LabInfoService service;

    // ------------------ REGISTER LAB ------------------
    @PostMapping("/register")
    public String register(@ModelAttribute LabInfo lab, Model model) {
        service.registerLab(lab);
        model.addAttribute("msg", "Lab Registered Successfully!");
        return "success";
    }

    @PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    HttpSession session,
                    Model model) {

    LabInfo lab = service.login(email, password);

    if (lab == null) {
        model.addAttribute("error", "Invalid email or password!");
        return "lab-login";
    }

    // 🔥 Store all important session details
    session.setAttribute("labId", lab.getId());
    session.setAttribute("labName", lab.getLabName());
    session.setAttribute("lab", lab);

    System.out.println("Login Success! Lab ID: " + lab.getId());
    System.out.println("Lab Name: " + lab.getLabName());

    // 🔥 Always use redirect after login
    return "redirect:/lab/dashboard";
}

    // ------------------ DASHBOARD ---------------------
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        LabInfo lab = (LabInfo) session.getAttribute("lab");

        if (lab == null) {
            return "redirect:/lab-login";
        }

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
    
    
}
