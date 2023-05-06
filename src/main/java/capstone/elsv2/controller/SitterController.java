package capstone.elsv2.controller;

import capstone.elsv2.dto.booking.request.AcceptBookingRequestDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.common.SearchDTO;
import capstone.elsv2.dto.sitter.*;
import capstone.elsv2.dto.workingTime.*;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.AssigningService;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.services.SitterService;
import capstone.elsv2.services.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("api/v1/sitter")
public class SitterController {
    @Autowired
    private SitterService sitterService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private WorkingTimeService workingTimeService;
    @Autowired
    private AssigningService assigningService;

    @GetMapping("admin/sitters/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> findAllSitter(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = sitterService.findAllSitter(pageNumber, pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PostMapping("admin/sitters-by-key-word")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> findAllSitterByKeyWord(@RequestBody SearchDTO searchDTO) {
        PageDTO pageDTO = sitterService.findAllSitterByKeyWord(searchDTO.getKeyWord(), searchDTO.getPageNumber(), searchDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }


    @PostMapping("admin/sitters-get-forms")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getSitterForm(@RequestBody SearchDTO searchDTO) {
        PageDTO pageDTO = sitterService.getAllFormSitter(searchDTO.getKeyWord(), searchDTO.getPageNumber(), searchDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("admin/sitters-activate-deactivate/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> findAllSitterForAdmin(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = sitterService.findAllSitterForAdmin(pageNumber, pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("admin/sitter/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> findSitterById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.findSitterById(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/ban/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> banSitter(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        Boolean isBan = sitterService.banSitter(id);
        if (isBan == true) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Nhân viên đã bị khóa không thể khóa nữa");
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/unban/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> unbanSitter(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        Boolean isUnBan = sitterService.unBanSitter(id);
        if (isUnBan == true) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Nhân viên đã mở khóa không thể mở khóa nữa");
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/register")
    @PermitAll
    public ResponseEntity<ResponseDTO> register(@RequestBody SitterRegisterDTO sitterRegisterDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.register(sitterRegisterDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    @PutMapping("mobile/contact/update")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> updateContact(@RequestBody UpdateSitterContactDTO updateSitterContactDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.updateContact(updateSitterContactDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/contact/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getContact(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.getContact(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/information/update")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> updateInformation(@RequestBody SitterInformationDTO sitterInformationDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.updateInformation(sitterInformationDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/information/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getInformation(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.getInformation(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/accept/{sitterId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> acceptSitter(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.acceptSitter(sitterId));
        responseDTO.setSuccessCode(SuccessCode.ACCEPT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/reject")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> rejectSitter(@RequestBody RejectSitterDTO rejectSitterDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.rejectSitter(rejectSitterDTO));
        responseDTO.setSuccessCode(SuccessCode.REJECT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("mobile/send-form/{id}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> sendForm(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterService.sendForm(id));
        responseDTO.setSuccessCode(SuccessCode.SEND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-all-by-package-and-sitter-status/{packageId}/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> findAllByPackageBySitterStatus(@PathVariable String packageId, @PathVariable String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(bookingService.getAllByPackageBySitterStatus(packageId, status, ""));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("sitter/get-all-message-sitter/{sitterId}")
//    @PreAuthorize("hasAnyRole('SITTER')")
//    public ResponseEntity<ResponseDTO> findAllMessageBySitterId(@PathVariable String sitterId) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(sitterService.getAllMessageBySitterId(sitterId));
//        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @PostMapping("mobile/add-working-time-week")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> addWorkingTimeWeek(@RequestBody AddWorkingTimeWeekDTO addWorkingTimeWeekDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workingTimeService.addWorkingTimeByWeek(addWorkingTimeWeekDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("mobile/deactivate-working-time/{workingTimeId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> deactivateWorkingTime(@PathVariable String workingTimeId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workingTimeService.deactivateWorkingTime(workingTimeId));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/activate-working-time/{workingTimeId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> activateWorkingTime(@PathVariable String workingTimeId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workingTimeService.activateWorkingTime(workingTimeId));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("mobile/get-all-working-time/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getAllWorkingTime(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workingTimeService.getAllWorkingTime(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-working-time/{workingTimeId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getWorkingTimeById(@PathVariable String workingTimeId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workingTimeService.getWorkingTimeById(workingTimeId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("admin/get-all-sitter-reject/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllSitterReject(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = sitterService.findAllSitterReject(pageNumber, pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }


    @PutMapping("mobile/accept-booking")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<?> acceptBooking(@RequestBody AcceptBookingRequestDTO acceptBookingRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(assigningService.acceptSitter(acceptBookingRequestDTO));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/waiting-assigned-booking-list/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<?> getWaitingAssignedBookingList(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(assigningService.getListWaitingBooking(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}


