package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.tracking.AddTrackingDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("api/v1/tracking")
public class TrackingController {

    @Autowired
    TrackingService trackingService;

    @PostMapping("mobile/add-tracking")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> addTracking(@RequestBody AddTrackingDTO addTrackingDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(trackingService.addTracking(addTrackingDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping ("mobile/get-tracking/{bookingDetailId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getTracking(@PathVariable String bookingDetailId ) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(trackingService.getTracking(bookingDetailId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-tracking/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getAllTracking(@PathVariable String bookingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(trackingService.getAllTracking(bookingId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
