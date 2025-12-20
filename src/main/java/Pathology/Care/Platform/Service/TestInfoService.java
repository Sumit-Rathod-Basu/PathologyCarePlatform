package Pathology.Care.Platform.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pathology.Care.Platform.Model.TestInfo;
import Pathology.Care.Platform.Repository.TestRepository;

@Service
public class TestInfoService {
      
    @Autowired
    private TestRepository repo;

    public TestInfo addTest(TestInfo test) {
        return repo.save(test);
    }

    
    public List<TestInfo> getTestsByLab(Long labId) {
        return repo.findByLabId(labId);
    }

    public TestInfo getById(Long id) {
        return repo.findById(id).orElse(null);
    }


    public TestInfo updateTest(TestInfo test) {
        return repo.save(test);
    }


    public void deleteTest(Long id) {
        repo.deleteById(id);
    }


    public TestInfo getTestById(Long id) {
        // TODO Auto-generated method stub
        return repo.findById(id).orElse(null);
    }


 public List<TestInfo> getAllPublishedTests() {
    return repo.findByPublish("PUBLISH");
}

}
