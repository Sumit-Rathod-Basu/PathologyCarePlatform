package Pathology.Care.Platform.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Pathology.Care.Platform.Model.ResultSummary;

@Repository
public interface ResultSummaryRepo  extends JpaRepository<ResultSummary, Long> {
   //ResultSummary findByTestEntryId(long testEntryId);
   List<ResultSummary> findByTestEntryId(Long testEntryId);

}
