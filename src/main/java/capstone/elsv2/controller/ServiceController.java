package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.common.SearchDTO;
import capstone.elsv2.dto.service.AddServiceDTO;
import capstone.elsv2.dto.service.ServiceDTO;
import capstone.elsv2.dto.service.ServiceDetailDTO;
import capstone.elsv2.dto.service.UpdateServiceDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.SerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/service")
public class ServiceController {
    @Autowired
    private SerService serService;

    @PostMapping("admin/create")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> createService(@RequestBody AddServiceDTO addServiceDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (serService.createService(addServiceDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thêm được dịch vụ");
        }
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> updateService(@RequestBody UpdateServiceDTO updateServiceDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (serService.updateService(updateServiceDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thay đổi được dịch vụ");
        }
    }

    @PatchMapping("admin/activate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> activateService(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (serService.activateService(id)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Dịch vụ đã được kích hoạt không thể kích hoạt thêm nữa");
        }
    }

    @PatchMapping("admin/deactivate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> deactivateService(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (serService.deactivateService(id)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Dịch vụ đã gở xuống không thể gỡ xuống nữa");
        }
    }

    @PostMapping("admin/services")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> findAllService(@RequestBody SearchDTO searchDTO) {
        PageDTO pageDTO = serService.findAllService(searchDTO.getPageNumber(), searchDTO.getPageSize());
        if (pageDTO != null) {
            pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
            return ResponseEntity.ok().body(pageDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy được dịch vụ nào nữa");
        }
    }

    @GetMapping("admin/services")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> findAllService() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(serService.findAllService());
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> findServiceById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        ServiceDetailDTO serviceDetailDTO = serService.findById(id);
        responseDTO.setData(serviceDetailDTO);
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
