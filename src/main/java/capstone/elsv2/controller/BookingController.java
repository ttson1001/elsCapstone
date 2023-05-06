package capstone.elsv2.controller;

import capstone.elsv2.dto.booking.*;
import capstone.elsv2.dto.booking.request.AddDateBookingDTO;
import capstone.elsv2.dto.booking.request.CheckPriceRequestDTO;
import capstone.elsv2.dto.booking.response.BookingResponseDTO;
import capstone.elsv2.dto.common.*;
import capstone.elsv2.emunCode.ErrorCode;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.services.AssigningService;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AssigningService assigningService;

    @PostMapping("mobile/createV2")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> addBookingV2(@RequestBody AddBookingV2DTO addBookingDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        BookingResponseDTO dto = bookingService.addBookingV2(addBookingDTO);
        responseDTO.setData(dto);
        //assign
        Booking booking = bookingService.getBooking(dto.getId());
        List<SitterProfile> listSitter =bookingService.getAvailableSitter(booking,addBookingDTO.getDistrict(),null);
        assigningService.assignSitter(booking,listSitter);
        System.out.println("Call assign Sitter Async Done");
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-all-booking-request/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllFormBooking(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = new PageDTO();
        pageDTO = bookingService.getAllFormBooking(pageNumber, pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PostMapping("admin/get-all-booking")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllBooking(@RequestBody PageParamDTO paramDTO) {
        PageDTO pageDTO = bookingService.getAllBooking(paramDTO.getPageNumber(), paramDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PostMapping("admin/get-all-booking-by-status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllBookingByStatusForAdmin(@RequestBody BookingStatusPageDTO bookingStatusPageDTO) {
        PageDTO pageDTO = bookingService.getAllByStatusForAdmin(bookingStatusPageDTO.getStatus(), bookingStatusPageDTO.getPageNumber(), bookingStatusPageDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("common/get-all-booking-by-status/{status}}")
    @PreAuthorize("hasAnyRole('SITTER','CUSTOMER','ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getAllBookingByStatus(@PathVariable String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllByStatus(status));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-booking-by-status-and-customer_id/{status}/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllBookingByStatusAndCustomerId(@PathVariable String status, @PathVariable String customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllByStatusAndCustomerId(status, customerId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-booking-by-status-and-sitter_id/{status}/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getAllBookingByStatusAndUserId(@PathVariable String status, @PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllByStatusAndSitterId(status, sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-form-booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getBookingFormById(@PathVariable String bookingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getBookingFormById(bookingId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("admin/get-cancel-form-booking/{bookingId}")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ResponseDTO> getBookingCancelFormById(@PathVariable String bookingId) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(bookingService.getCancelFormBooking(bookingId));
//        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @GetMapping("common/get-full-detail-booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getFullDetailBooking(@PathVariable String bookingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getBookingFullDetail(bookingId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("admin/get-suggest-sitter-for-booking/{bookingId}")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ResponseDTO> getSuggestSitter(@PathVariable String bookingId) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(bookingService.getSuggestSitter(bookingId));
//        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @PutMapping("admin/assign-sitter/{bookingId}/{sitterId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> assignSitterIntoBooking(@PathVariable String bookingId, @PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.assignSitterIntoBooking(sitterId, bookingId));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    // check in
    @PutMapping("mobile/check-in")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> checkIn(@RequestBody CheckInDTO checkInDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.checkIn(checkInDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    // check out

    @PutMapping("mobile/check-out")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> checkOut(@RequestBody CheckOutDTO checkOutDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.checkOut(checkOutDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-schedule-customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getScheduleCustomer(@PathVariable String customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingScheduleCustomer(customerId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-schedule-booking-customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getScheduleBookingCustomer(@PathVariable String customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingScheduleCustomerPresent(customerId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-schedule-booking-sitter/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getScheduleBookingSitter(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingScheduleSitterPresent(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-schedule-sitter/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getScheduleSitter(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingScheduleSitter(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    //bớt lịch
    @PutMapping("mobile/reduce-booking-date")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> reduceBookingDate(@RequestBody ReduceBookingDateDTO reduceBookingDateDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.reduceBooking(reduceBookingDateDTO));
        responseDTO.setSuccessCode(SuccessCode.REDUCE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

//    @PatchMapping("mobile/cancel-booking-details/{bookingDetailId}/{bookingId}")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    public ResponseEntity<ResponseDTO> cancelBookingDetail(@PathVariable String bookingDetailId, @PathVariable String bookingId) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(bookingService.cancelBookingDetail(bookingDetailId, bookingId));
//        responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }

    //customer cancel booking
    @PutMapping("mobile/customer-cancel-booking")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> cancelBookingByCustomer(@RequestBody BookingCancelDTO bookingCancelDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.cancelBookingForCustomer(bookingCancelDTO));
        responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/sitter-cancel-booking")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> cancelBookingBySitter(@RequestBody BookingCancelDTO bookingCancelDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.cancelBookingForSitter(bookingCancelDTO));
        responseDTO.setSuccessCode(SuccessCode.CANCEL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-full-booking-history/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getBookingHistory(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getFullBookingHistory(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("admin/get-all-booking-history")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllBookingHistory(@RequestBody PageParamDTO paramDTO) {
        PageDTO pageDTO = bookingService.getAllBookingHistory(paramDTO.getPageNumber(), paramDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("mobile/get-all-booking-history-by-customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<ResponseDTO> getAllBookingHistoryByCustomer(@PathVariable String customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingHistoryByCustomer(customerId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-booking-history-by-sitter/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER','ADMIN')")
    public ResponseEntity<ResponseDTO> getAllBookingHistoryBySitter(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingHistoryBySitter(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/check-price")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> checkPrice(@RequestBody CheckPriceRequestDTO checkPriceRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(ValidateUtil.isNullOrEmpty(checkPriceRequestDTO.getDate())){
                responseDTO.setErrorCode(ErrorCode.INVAlID_REQUEST);
                responseDTO.setData("Invalid date list");
                return ResponseEntity.badRequest().body(responseDTO);
            }
            if(ValidateUtil.isNullOrEmpty(checkPriceRequestDTO.getPackageId())){
                responseDTO.setErrorCode(ErrorCode.INVAlID_REQUEST);
                responseDTO.setData("Invalid package id");
                return ResponseEntity.badRequest().body(responseDTO);
            }
            responseDTO.setData(bookingService.checkDateRangePrice(checkPriceRequestDTO));
            responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        }catch(ResponseStatusException e){
            responseDTO.setData(e.getReason());
            responseDTO.setErrorCode(ErrorCode.ERROR);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        catch(Exception e){
            responseDTO.setData(e.getMessage());
            responseDTO.setErrorCode(ErrorCode.ERROR);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/check-reduce-day/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> checkReduceDay(@PathVariable String bookingId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.checkReduceDate(bookingId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/check-add-day/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> checkAddDay(@PathVariable String bookingId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.checkAddDate(bookingId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/changeSitter/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> changeSitter(@PathVariable String bookingId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.changeSitter(bookingId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-booking-in-present-for-sitter/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getBookingInPresentForSitter(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllBookingInPresent(sitterId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/addDate")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> addDateBooking(@RequestBody AddDateBookingDTO addDateBookingDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.addDateBooking(addDateBookingDTO));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/check-date-check-in-out/{bookingId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> checkDate(@PathVariable String bookingId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.checkDate(bookingId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/assignSitter/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> assignSitter(@PathVariable String bookingId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
