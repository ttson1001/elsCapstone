package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.education.AddEducationDTO;
import capstone.elsv2.dto.education.EducationDTO;
import capstone.elsv2.dto.education.UpdateEducationDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/education")
public class EducationController {
    @Autowired
    EducationService educationService;

    @PostMapping("mobile/create")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> addEducation(@RequestBody AddEducationDTO addEducationDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(educationService.addEducation(addEducationDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/update")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> updateEducation(@RequestBody UpdateEducationDTO updateEducationDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(educationService.updateEducation(updateEducationDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/educations/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> findAllEducation(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        List<EducationDTO> educationDTOS = educationService.findAllBySitterId(sitterId);
        responseDTO.setData(educationDTOS);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> findEducationById(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(educationService.findById(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("mobile/remove/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> removeEducation(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(educationService.removeEducation(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}

