package Pathology.Care.Platform.Service;
import Pathology.Care.Platform.Model.LabPatient;
import Pathology.Care.Platform.Model.Patient;

import Pathology.Care.Platform.Repository.patientrepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Patientservice {

    @Autowired
    private  patientrepo patientrepo;


    public void savePatient(Patient entity) {
        
        patientrepo.save(entity);
    }

   public Patient login(Patient request) {
    Patient p = patientrepo.findByEmail(request.getEmail());

    if (p != null && p.getPassword().equals(request.getPassword())) {  
        // password is case-sensitive automatically
        return p;
    }

    return null;
}



}
