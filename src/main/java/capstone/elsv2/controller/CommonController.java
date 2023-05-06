package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.CommonService;
//import capstone.elsv2.utils.ListLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("api/v1/common")
public class CommonController {

    @Autowired
    CommonService commonService;
//    @Autowired
//    ListLocation listLocation;

//    @PutMapping("admin/update-deposit/{deposit}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ResponseDTO> updateDeposit(@PathVariable float deposit){
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(commonService.updateDeposit(deposit));
//        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @PutMapping("admin/update-commission/{commission}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateCommission(@PathVariable float commission) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.updateCommission(commission));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/update-mid-night/{midNight}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateMidNight(@PathVariable float midNight) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.updateMidNight(midNight));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/update-weekend/{weekend}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateWeekend(@PathVariable float weekend) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.updateWeekend(weekend));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/update-holiday/{holiday}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateHoliday(@PathVariable float holiday) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.updateHoliday(holiday));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-deposit-and-commission")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getDepositAndCommission() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.getDepositAndCommission());
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-slot")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','SITTER','CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllSlots() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.getAllSlot());
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-health-status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','SITTER','CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllHealthStatus() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.getAllHealthStatus());
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/notification-message/{otherId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getNotification(@PathVariable String otherId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(commonService.sendNotificationMessage(otherId));
        responseDTO.setSuccessCode(SuccessCode.SEND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-city")
    @PermitAll
    public ResponseEntity<ResponseDTO> getCity() {
        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(listLocation.getCity());
        responseDTO.setData(null);
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-district")
    @PermitAll
    public ResponseEntity<ResponseDTO> getDistrict() {
        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(listLocation.getListDistrict());
        responseDTO.setData(null);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-ward-by-district/{districtName}")
    @PermitAll
    public ResponseEntity<ResponseDTO> getWardByDistrict(@PathVariable String districtName) {
        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(listLocation.findAllWardByDistrict(districtName));
        responseDTO.setData(null);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
