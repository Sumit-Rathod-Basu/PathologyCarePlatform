package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Pathology.Care.Platform.Model.LabPatient;
import Pathology.Care.Platform.Service.LabPatientServices;




@Controller
@RequestMapping("/")
public class PageshandlingControler {

    @Autowired
    private LabPatientServices patientService;

   @GetMapping("/")
   public String getMethodName() {
       return "index";
   }

      @GetMapping("/labs/login")
   public String PatientsDashbord() {
         return "lab-login";
   }
   @GetMapping("/lab")
   public String Patientlogin() {
         return "lab-dashboard";
   }
     @GetMapping("/patient/login")
public String showLogin(Model model) {
    return "patient-login";
}



@GetMapping("/labs/register")
public String labsregitser(Model model  ) {
    return "lab-regester";
}
@GetMapping("/lab/profile/edit")
public String updationprofile(Model model  ) {
    return "edit-profile";
}

@GetMapping("/report")
public String getMethodName(Model model  ) {
    return "Pathology-patient-resetration";
}
@GetMapping("/Billing-dashbord")
public String billingDashboard(@RequestParam("patientId") Long patientId, Model model) {
    LabPatient patient = patientService.getPatientById(patientId);
    model.addAttribute("patient", patient);
    return "billing-page";
}
@GetMapping("/load")
public String getInvoice( @RequestParam("patientId") Long patientId, Model model) {
    LabPatient patient = patientService.getPatientById(patientId);
    System.out.println("Invoice for Patient ID: " + patientId);
    model.addAttribute("patient", patient);
    return "invoice";
}
/* @GetMapping("/print-invoice")
public String finalinvoice(@RequestParam("patientId") Long patientId, Model model) {
    LabPatient patient = patientService.getPatientById(patientId);
    System.out.println("Invoice for Patient ID: " + patientId);
    model.addAttribute("patient", patient);
    return "invoice";
}
 */

}
