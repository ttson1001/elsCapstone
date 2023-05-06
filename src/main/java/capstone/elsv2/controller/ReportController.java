package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.report.AddReportDTO;
import capstone.elsv2.dto.report.ReportReplyDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("mobile/customer-to-sitter")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> addReportCustomerToSitter(@RequestBody AddReportDTO addReportDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.addReportCustomerToSitter(addReportDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/sitter-to-customer")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> addReportSitterToCustomer(@RequestBody AddReportDTO addReportDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.addReportSitterToCustomer(addReportDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/reply")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> replyReport(@RequestBody ReportReplyDTO reportReplyDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.replyReport(reportReplyDTO));
        responseDTO.setSuccessCode(SuccessCode.SEND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-for-customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllReportForCustomer(@PathVariable String customerId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.getAllReportForCustomer(customerId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-for-sitter/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getAllReportForSitter(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.getAllReportForSitter(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-all-form/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllReportForm(@PathVariable int pageNumber, @PathVariable int pageSize){
        PageDTO pageDTO = reportService.getAllFormReport(pageNumber,pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("admin/get-detail/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getReportDetail(@PathVariable String reportId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.getReportDetail(reportId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
