package Pathology.Care.Platform.Cotroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//import Pathology.Care.Platform.Model.LabInfo;
import Pathology.Care.Platform.Model.LabPatient;
import Pathology.Care.Platform.Model.Patient;

import Pathology.Care.Platform.Service.LabPatientServices;
import jakarta.servlet.http.HttpSession;


@Controller
public class LabPatientsController {
     @Autowired
private LabPatientServices patientService;




@GetMapping("/register/patinets")//register/patients/
public String showRegisterForm( Model model,HttpSession session) {
    model.addAttribute("patient", new Patient());
   Long id = (Long) session.getAttribute("labId");
   System.out.println("Lab ID in session: " + id);
    model.addAttribute("labid", id);
    return "Pathology-patient-resetration";
}



@PostMapping("/register")
public String registerPatient(LabPatient patient) {
    //LabInfo labInfo = labInfoService.getLabInfo(); // Fetch lab info
    LabPatient savedPatient = patientService.savePatient(patient); // save and get persisted patient
    return "redirect:/Billing-dashbord?patientId=" + savedPatient.getId();
}

@GetMapping("/list/labpatients")
    public String listPatients(Model model,HttpSession session) {
       
          Long id = (Long) session.getAttribute("labId");
        List<LabPatient> patients =patientService.getAllPatients(id);
        model.addAttribute("patients", patients);

        return "Lab_patients_list";   // Thymeleaf page name
    }

}
