package Pathology.Care.Platform.Cotroller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import Pathology.Care.Platform.Model.ResultSummary;
import Pathology.Care.Platform.Service.ResultSummarysave;

@Controller
public class ResultSummaryContro {

    @Autowired
    private ResultSummarysave services;

    @PostMapping("/api/resultsummaries")
    public String saveSummary(ResultSummary entity,Model model) {
        services.saveSummary(entity);
      // return "redirect:/invoice/" + entity.getTestEntryId(); // or the page you want to open
    model.addAttribute("patientId", "PT1023");
    model.addAttribute("reportId", "RP5587");
    model.addAttribute("generatedDate", LocalDate.now());
      return "Report-complete";
    }
}
