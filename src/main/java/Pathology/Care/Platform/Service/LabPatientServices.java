package Pathology.Care.Platform.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pathology.Care.Platform.Model.LabPatient;

import Pathology.Care.Platform.Repository.LabPatientRepo;
@Service
public class LabPatientServices
{
    @Autowired
private LabPatientRepo patientRepository;



public LabPatient savePatient(LabPatient patient) {
return patientRepository.save(patient);
}



public List<LabPatient> getAllPatients(long id) {
return patientRepository.findAllByLabId(id);
}



public LabPatient getPatientById(Long patientId) {
    // TODO Auto-generated method stub
    return patientRepository.findById(patientId).orElse(null);
}

}
