package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.LabSettings;
import Pathology.Care.Platform.Service.CloudinaryService;
import Pathology.Care.Platform.Service.LabSettingsService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/settings")
public class LabSettingsController {

    @Autowired
    private LabSettingsService settingsService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Load Page
    @GetMapping
    public String settingsPage(Model model,HttpSession session) {
        String labName = (String) session.getAttribute("labName");
        System.out.println("Lab Name from Session: " + labName); // Debugging line
        model.addAttribute("settings", settingsService.getSettings(labName)); // Replace with actual lab name or logic to get it
        return "settings";
    }

    // Save Settings
    @PostMapping("/save")
    public String saveSettings(
            @RequestParam String labName,
            @RequestParam String doctorName,
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("seal") MultipartFile seal,
            @RequestParam("signature") MultipartFile signature,
            @RequestParam("letterhead") MultipartFile letterhead
    ) {

        LabSettings settings = settingsService.getSettings(labName); // Replace with actual lab name or logic to get it

        settings.setLabName(labName);
        settings.setDoctorName(doctorName);

        // Upload to Cloudinary and save URL
        if (!logo.isEmpty()) {
            settings.setLogoUrl(cloudinaryService.uploadFile(logo));
        }

        if (!seal.isEmpty()) {
            settings.setSealUrl(cloudinaryService.uploadFile(seal));
        }

        if (!signature.isEmpty()) {
            settings.setSignatureUrl(cloudinaryService.uploadFile(signature));
        }

        if (!letterhead.isEmpty()) {
            settings.setLetterheadUrl(cloudinaryService.uploadFile(letterhead));
        }

        settingsService.saveSettings(settings);

        return "redirect:/admin/settings";
    }
}