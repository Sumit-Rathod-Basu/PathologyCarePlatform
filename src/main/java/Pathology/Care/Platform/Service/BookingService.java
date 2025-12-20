package Pathology.Care.Platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import Pathology.Care.Platform.Model.BookingInfo;
import Pathology.Care.Platform.Repository.BookingRepo;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    public BookingInfo saveBooking(BookingInfo booking) {
        return bookingRepo.save(booking);
    }

    public List<BookingInfo> getAllBookings() {
        return bookingRepo.findAll();
    }
    
    public List<BookingInfo> getBookingsByLab(Long labId) {
        return bookingRepo.findByLabId(labId);
    }

    public BookingInfo getBookingById(Long id) {
        // TODO Auto-generated method stub
        return bookingRepo.findById(id).orElse(null);   
    }
}
