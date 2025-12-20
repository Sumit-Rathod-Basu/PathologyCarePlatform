package Pathology.Care.Platform.Service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pathology.Care.Platform.Model.ResultSummary;
import Pathology.Care.Platform.Repository.ResultSummaryRepo;

@Service
public class ResultSummarysave {

    @Autowired
    private ResultSummaryRepo repo;

    public  ResultSummary saveSummary(ResultSummary summary) {
    return repo.save(summary);
}



public List<ResultSummary> getByTestEntryId(Long testEntryId) {
        return repo.findByTestEntryId(testEntryId);
    }



}
