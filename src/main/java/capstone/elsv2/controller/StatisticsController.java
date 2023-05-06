package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.entities.WorkingTime;
import capstone.elsv2.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/statistics")
public class StatisticsController {

    @Autowired
    CusService cusService;
    @Autowired
    SitterService sitterService;
    @Autowired
    BookingService bookingService;
    @Autowired
    ElderService elderService;

    @Autowired
    AdminService adminService;

    @GetMapping("admin/dash-board-information")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getInformationDashBoard() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getInformationDashBoard());
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-day/{localDate}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueForDate(@PathVariable String localDate) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForDate(LocalDate.parse(localDate)));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-month/{month}/{year}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueForDateInMonth(@PathVariable Integer month, @PathVariable Integer year) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForDateInMonth(month, year));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueForAllMonthInYear(@PathVariable int year) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForAllMonthInYear(year));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-sitter-day/{localDate}/{sitterId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueSitterForDate(@PathVariable String localDate, @PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueSitterForDate(LocalDate.parse(localDate), sitterId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-sitter-month/{month}/{year}/{sitterId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueForDateInMonth(@PathVariable Integer month, @PathVariable Integer year, @PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForDateInMonth(month, year, sitterId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/revenue-sitter-year/{year}/{sitterId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getRevenueForAllMonthInYear(@PathVariable int year, @PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForAllMonthInYear(year, sitterId));
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
