package Pathology.Care.Platform.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pathology.Care.Platform.Model.Bill;
import Pathology.Care.Platform.Repository.BillRepo;

@Service
public class BillingService {
    @Autowired
private BillRepo billRepository;

   
    // Save single bill
    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    // Fetch all bills (optional)
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // Fetch bill by ID (optional)
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    public List<Bill> getPatientById(Long id) {
        // TODO Auto-generated method stub
       List<Bill> bills = billRepository.findByPatientId(id);
       return bills;
    }
    }
