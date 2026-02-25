package Pathology.Care.Platform.Service;

import Pathology.Care.Platform.Model.*;
import Pathology.Care.Platform.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {
   
    @Autowired
    private  LabPatientRepo patientRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private  BillRepo billRepo;
    @Autowired
    private  TestRepository testRepo;

    public DashboardService(LabPatientRepo patientRepo,
                            BookingRepo bookingRepo,
                            BillRepo billRepo,
                            TestRepository testRepo) {
        this.patientRepo = patientRepo;
        this.bookingRepo = bookingRepo;
        this.billRepo = billRepo;
        this.testRepo = testRepo;
    }

    public Long getTotalPatients(Long labId) {
        return patientRepo.countByLabId(labId);
    }

    public List<BookingInfo> getTotalBookings(Long labId) {
        return bookingRepo.findByLabId(labId);
    }

    public List<TestInfo> getTotalTests(Long labId) {
        return testRepo.findByLabId(labId);
    }

public Double getTotalRevenue(Long labId) {
    // Get all patient IDs for this lab
    List<Long> patientIds = (List<Long>) patientRepo.findAllPatientIdsByLabId(labId);

    // If no patients, revenue is 0
    if (patientIds.isEmpty()) {
        return 0.0;
    }

    // Get all bills for these patients
    List<Bill> bills = billRepo.findByPatientIdIn(patientIds);

    // Sum up totalPrice from all bills
    double totalRevenue = bills.stream()
                               .mapToDouble(Bill::getTotalPrice)
                               .sum();

    return totalRevenue;  // Return the calculated total revenue
}
}