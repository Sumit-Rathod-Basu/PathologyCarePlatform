package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import Pathology.Care.Platform.Model.ResultSummary;
import Pathology.Care.Platform.Service.ResultSummarysave;

@Controller
public class ResultSummaryContro {

    @Autowired
    private ResultSummarysave services;

    @PostMapping("/api/resultsummaries")
    public String saveSummary(ResultSummary entity) {
        services.saveSummary(entity);
       return "redirect:/invoice/" + entity.getTestEntryId(); // or the page you want to open
    }
}
