package Pathology.Care.Platform.Service;
import Pathology.Care.Platform.Model.LabSettings;
import Pathology.Care.Platform.Repository.LabSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LabSettingsService {

    @Autowired
    private LabSettingsRepository repo;
    
    public LabSettings saveSettings(LabSettings settings) {
        return repo.save(settings);
    } 
    
public LabSettings getSettings(String labName) {
    return repo.findByLabName(labName)
               .orElse(new LabSettings());
}

}