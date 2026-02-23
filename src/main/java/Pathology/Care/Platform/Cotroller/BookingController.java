package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.BookingInfo;
import Pathology.Care.Platform.Model.Patient;
import Pathology.Care.Platform.Model.TestInfo;
import Pathology.Care.Platform.Service.BookingService;
import Pathology.Care.Platform.Service.TestInfoService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TestInfoService testService;

    // Show booking page
    @GetMapping("/book-test/{id}")
    public String bookTestPage(@PathVariable Long id, Model model) {

        TestInfo test = testService.getTestById(id);
        model.addAttribute("test", test);

        BookingInfo booking = new BookingInfo();
        booking.setTestId(test.getId());
        booking.setTestName(test.getTestName());
        booking.setLabId(test.getLabId());
        booking.setLabName(test.getLabName());

        model.addAttribute("booking", booking);

        return "book-test";
    }

    // Handle Booking Submit
 @PostMapping("/book-test/save")
public String saveBooking(@ModelAttribute BookingInfo booking, HttpSession session) {

    // Get patient object from session
    Patient patient = (Patient) session.getAttribute("loggedInPatient");
    if (patient == null) {
        return "redirect:/patient/login"; // redirect if not logged in
    }
    System.out.println("PRICE RECEIVED: " + booking.getPrice());

    // Set patient details in booking
    booking.setPatientId(patient.getId());
  
    // Save booking
    bookingService.saveBooking(booking);

    return "redirect:/booking-success";
}


    @GetMapping("/booking-success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/lab/BookingDashboard")
public String labDashboard(HttpSession session, Model model) {

    Long labId = (Long) session.getAttribute("labId");
    String labName = (String) session.getAttribute("labName");

    if (labId == null) {
        return "redirect:/labs/login";
    }

    List<BookingInfo> bookings = bookingService.getBookingsByLab(labId);

    model.addAttribute("labName", labName);
    model.addAttribute("labid", labId);
    model.addAttribute("bookings", bookings);

    return "Labs-Bokking";
}
    @GetMapping("/booking/view/{id}")
    public String viewBookingDetails(@PathVariable Long id, Model model) {

        BookingInfo booking = bookingService.getBookingById(id);

        if (booking == null) {
            model.addAttribute("error", "Booking not found!");
            return "error-page";    // Optional error page
        }

        model.addAttribute("booking", booking);
        return "booking-details";   // HTML page name
    }
    
    @GetMapping("/last-booking")
    public String getLastBooking(HttpSession session, Model model) {

        Patient patient = (Patient) session.getAttribute("loggedInPatient");

        if (patient == null) {
            return "redirect:/login";
        }

        List<BookingInfo> booking = bookingService
                . getAllBooking(patient.getEmail());
   System.out.println("Your Mail is:"+patient.getEmail());
        model.addAttribute("booking", booking);

        return "dummy";
    }

}

