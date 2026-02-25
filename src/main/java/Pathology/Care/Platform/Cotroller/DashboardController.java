package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/lab-dashboard/analytics")
    public String labDashboard(@RequestParam("labId") Long labId, Model model) {
        // Total patients count
        Long totalPatients = dashboardService.getTotalPatients(labId);

        // Total bookings count
        int totalBookings = dashboardService.getTotalBookings(labId).size();

        // Total tests count
        int totalTests = dashboardService.getTotalTests(labId).size();

        // Total revenue
        Double totalRevenue = dashboardService.getTotalRevenue(labId);

        // Add counts to model
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalTests", totalTests);
        model.addAttribute("totalRevenue", totalRevenue);

        return "lab-analitics-dashboard";  // Thymeleaf template
    }
}