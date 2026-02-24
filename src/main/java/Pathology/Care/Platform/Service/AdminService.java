package Pathology.Care.Platform.Service;


import org.springframework.stereotype.Service;
import Pathology.Care.Platform.Model.*;
import Pathology.Care.Platform.Repository.AdminRepo;
import Pathology.Care.Platform.Repository.LabInfoRepository;
import Pathology.Care.Platform.Repository.patientrepo;
import java.util.List;

@Service
public class AdminService {

    private final patientrepo patientRepo;
    private final LabInfoRepository labRepo;
    private final AdminRepo adminRepo;

    public AdminService(patientrepo patientRepo, LabInfoRepository labRepo, AdminRepo adminRepo) {
        this.patientRepo = patientRepo;
        this.labRepo = labRepo;
        this.adminRepo = adminRepo;
    }

    // Counts
    public long getTotalPatients() { return patientRepo.count(); }
    public long getTotalLabs() { return labRepo.count(); }
    public long getTotalAdmins() { return adminRepo.count(); }

    // Admin CRUD
    public List<AdminUser> getAllAdmins() { return adminRepo.findAll(); }
    public AdminUser createAdmin(AdminUser admin)
     { 
        return adminRepo.save(admin); 

     }
    public AdminUser updateAdmin(AdminUser admin) { return adminRepo.save(admin); }
    public void deleteAdmin(Long id) { adminRepo.deleteById(id); }

    // Lists
    public List<Patient> getAllPatients() { return patientRepo.findAll(); }
    public List<LabInfo> getAllLabs() { return labRepo.findAll(); }

    public AdminUser saveAdmin(AdminUser admin) {
        return adminRepo.save(admin);
    }
}