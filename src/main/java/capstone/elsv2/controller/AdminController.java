package capstone.elsv2.controller;

import capstone.elsv2.dto.auth.StaffAccountDTO;
import capstone.elsv2.dto.auth.UpdateStaffDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.common.SearchDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("create-staff-account")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> createStaffAccount(@RequestBody StaffAccountDTO staffAccountDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.createStaffAccount(staffAccountDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-staff-account")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateStaffAccount(@RequestBody UpdateStaffDTO staffAccountDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.updateStaff(staffAccountDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("get-all-staffs-by-keyword")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PageDTO> getAllStaffByKeyWord(@RequestBody SearchDTO searchDTO){
        PageDTO pageDTO ;
        pageDTO = adminService.getAllStaff(searchDTO.getPageNumber(),searchDTO.getPageSize(),searchDTO.getKeyWord());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PatchMapping("ban-staff/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> banStaff(@PathVariable String staffId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.banStaff(staffId));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("unban-staff/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> unBanStaff(@PathVariable String staffId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.unBanStaff(staffId));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-staff/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getStaffById(@PathVariable String staffId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getStaffById(staffId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
