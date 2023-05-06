package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.elder.*;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/elder")
public class ElderController {
    @Autowired
    private ElderService elderService;

    @PostMapping("mobile/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createElder(@RequestBody AddElderDTO addElderDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(elderService.createElder(addElderDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/add-relationship")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> addRelationship(@RequestBody AddRelationshipDTO addRelationshipDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(elderService.addRelationShip(addRelationshipDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateElder(@RequestBody UpdateElderDTO updateElderDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (elderService.updateElder(updateElderDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm người người thân");
        }
    }

    @PatchMapping("mobile/remove/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> removeElder(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (elderService.removeElder(id)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể xóa người người thân");
        }
    }

    @GetMapping("mobile/elders/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> findAllElderByCustomer(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<ElderHealStatusDTO> elderDTOList = elderService.findAllByCustomerId(id);
        if (!elderDTOList.isEmpty() || elderDTOList != null) {
            responseDTO.setData(elderDTOList);
            responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người người thân nào");
        }
    }

    @GetMapping("mobile/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> findElderById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        ElderDetailDTO elderDetailDTO = elderService.findById(id);
        if (elderDetailDTO != null) {
            responseDTO.setData(elderDetailDTO);
            responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người người thân nào");
        }
    }

    @GetMapping("mobile/get_all_health_status_and_customer/{healthStatus}/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllByHealStatusAndCustomerId(@PathVariable String healthStatus, @PathVariable String customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<ElderDTO> elderDTOList = elderService.findAllByHealStatusAndCustomerId(customerId, healthStatus);
        responseDTO.setData(elderDTOList);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/check_exist_elder/{idCardNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> checkExistElder(@PathVariable String idCardNumber) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(elderService.checkElderExist(idCardNumber));
        responseDTO.setSuccessCode(SuccessCode.CHECK_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/find-by/{idCardNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> findByIdCardNumber(@PathVariable String idCardNumber) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(elderService.findByIdNumberCard(idCardNumber));
        responseDTO.setSuccessCode(SuccessCode.CHECK_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
