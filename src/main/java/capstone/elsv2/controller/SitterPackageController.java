package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.sitterpackage.AddSitterPackageDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.services.SitterPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sitter_package")
public class SitterPackageController {
    @Autowired
    private SitterPackageService sitterPackageService;


    @PostMapping("mobile/add")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> addSitterIntoPackage(@RequestBody AddSitterPackageDTO addSitterPackageDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterPackageService.addSitterInPackage(addSitterPackageDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-package-sitter-not-have/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> findPackageSitterNotHave(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterPackageService.findAllPackageSitterNotHave(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-package-sitter-have/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> findPackageSitterHave(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(sitterPackageService.findAllPackageBySitterId(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
