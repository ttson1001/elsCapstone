package capstone.elsv2.services;

import capstone.elsv2.dto.booking.*;
import capstone.elsv2.dto.booking.request.AddDateBookingDTO;
import capstone.elsv2.dto.booking.request.CheckPriceRequestDTO;
import capstone.elsv2.dto.booking.response.BookingResponseDTO;
import capstone.elsv2.dto.booking.response.CheckPriceResponseDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.sitter.SitterDTO;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.SitterProfile;

import java.util.List;

public interface BookingService {
    BookingResponseDTO addBookingV2(AddBookingV2DTO addBookingDTO) ;
    PageDTO getAllFormBooking(int pageNumber, int pageSize);
    PageDTO getAllBooking(int pageNumber, int pageSize);
    PageDTO getAllByStatusForAdmin(String status, int pageNumber, int pageSize);
    List<BookingDTO> getAllByStatus(String status);
    List<BookingDTO> getAllByStatusAndCustomerId(String status, String customerId);
    PageDTO getAllBookingHistory(int pageNumber, int pageSize);
    List<BookingDTO> getAllBookingHistoryByCustomer(String customerId);
    List<BookingDTO> getAllBookingHistoryBySitter(String sitterId);
    BookingFullHistoryDTO getFullBookingHistory(String id);
    List<BookingDTO> getAllByStatusAndSitterId(String status, String sitterId);
    List<SitterDTO> getAllByPackageBySitterStatus(String packageId, String status, String sitterId);
    BookingFormDTO getBookingFormById(String bookingId);
    BookingFullDetailDTO getBookingFullDetail(String bookingId);
    List<BookingScheduleCustomerDTO> getAllBookingScheduleCustomer(String customerId);
    List<BookingDTO> getAllBookingInPresent(String sitterId);
    List<BookingScheduleCustomerPresentDTO> getAllBookingScheduleCustomerPresent(String customerId);
    List<BookingScheduleSitterPresentDTO> getAllBookingScheduleSitterPresent(String sitterId);
    List<BookingScheduleSitterDTO> getAllBookingScheduleSitter(String sitterId);
    Boolean assignSitterIntoBooking(String sitterId, String bookingId);
    Boolean checkIn(CheckInDTO checkInDTO);
    Boolean checkOut(CheckOutDTO checkOutDTO);
    Boolean reduceBooking(ReduceBookingDateDTO reduceBookingDateDTO);
    BookingResponseDTO cancelBookingForCustomer(BookingCancelDTO bookingCancelDTO);
    Boolean cancelBookingForSitter(BookingCancelDTO bookingCancelDTO);
//    BookingCancelFormDTO getCancelFormBooking(String bookingId);
    CheckPriceResponseDTO checkDateRangePrice(CheckPriceRequestDTO checkPriceRequestDTO);

    CheckReduceDateDTO checkReduceDate(String bookingId);

    CheckAddDateDTO checkAddDate(String bookingId);

    BookingResponseDTO changeSitter(String bookingId);

    BookingResponseDTO addDateBooking(AddDateBookingDTO dto);

    String checkDate(String bookingId);

    Boolean cancelBooking(Booking booking);

    List<SitterProfile> getAvailableSitter(Booking booking, String district,String sitterId);
    Booking getBooking(String bookingId);
    Booking bookingWithoutWallet(AddBookingV2DTO addBookingDTO);

}
