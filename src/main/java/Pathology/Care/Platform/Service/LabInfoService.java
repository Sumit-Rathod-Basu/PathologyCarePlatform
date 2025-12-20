package Pathology.Care.Platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Repository.LabInfoRepository;

@Service
public class LabInfoService {

    @Autowired
    private LabInfoRepository repo;

    // REGISTER LAB
    public LabInfo registerLab(LabInfo lab) {
        return repo.save(lab);
    }

    // LOGIN LAB
    public LabInfo login(String email, String password) {
        return repo.findByEmailAndPassword(email, password);
    }
    // UPDATE LAB
    public LabInfo updateLab(LabInfo lab) {
        return repo.save(lab);
    }

    public Object getAllLabs() {
        // TODO Auto-generated method stub
        return repo.findAll();
    }

}
