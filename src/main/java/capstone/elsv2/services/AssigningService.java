package capstone.elsv2.services;

import capstone.elsv2.dto.booking.request.AcceptBookingRequestDTO;
import capstone.elsv2.dto.booking.response.BookingResponseDTO;
import capstone.elsv2.dto.booking.response.WaitingBookingResponseDTO;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.SitterProfile;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface AssigningService {

    void assignSitter(Booking booking, List<SitterProfile> listPrioriry);

    Boolean acceptSitter(AcceptBookingRequestDTO dto);

    List<WaitingBookingResponseDTO> getListWaitingBooking(String sitterId);

    Boolean stopAssignQueue(Booking booking);

    Boolean isAvailableSitter(String sitterId, Booking booking);
}
