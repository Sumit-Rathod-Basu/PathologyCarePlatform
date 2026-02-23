package Pathology.Care.Platform.Service;

import Pathology.Care.Platform.Model.TestEntry;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Pathology.Care.Platform.Repository.TestEntryRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestEntryService {
 
    @Autowired
    private  TestEntryRepo repo;

    // Save one test entry
    public TestEntry saveTest(TestEntry testEntry) {
        return repo.save(testEntry);
    }

    // Save multiple test entries
    public List<TestEntry> saveAllTests(List<TestEntry> testEntries) {
        return repo.saveAll(testEntries);
    } 



    // Get all test entries
    public List<TestEntry> getAllTestsByLabID(Long id) {
      return repo.findLatestDistinctPatientsByLabId(id);
    }

    public List<TestEntry> getAllTestsByEmail(String email) {
         return repo.findTopByPatientEmailOrderByIdDesc(email);
    }

    public List<TestEntry> getAllTestsByEmail1(String email) {
    return repo.findTopByPatientEmailOrderByIdDesc(email);
    }

    // Get single test entry
    public TestEntry getTest(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test Entry Not Found"));
    }

    // Delete a test entry
    public void deleteTest(Long id) {
        repo.deleteById(id);
    }

    // Update test entry
    public TestEntry updateTest(Long id, TestEntry updated) {

        TestEntry old = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test Entry Not Found"));

        old.setSrNo(updated.getSrNo());
        old.setTestField(updated.getTestField());
        old.setTestValue(updated.getTestValue());
        old.setUnit(updated.getUnit());
        old.setTestRange(updated.getTestRange());

        return repo.save(old);
    }

    public List<TestEntry> getTestsByPatientId(String patientId) {
        // TODO Auto-generated method stub
        
        return repo.findAll().stream().filter(test -> test.getPatientId().equals(patientId)).toList();
    }

    public List<TestEntry> getTestsById(String id) {
        // TODO Auto-generated method stub
        Long idLong = Long.parseLong(id);
         return repo.findAll().stream().filter(test -> test.getPatientId().equals(idLong)).toList();
    }

}
