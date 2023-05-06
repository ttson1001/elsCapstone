package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.workexp.AddWorkExpDTO;
import capstone.elsv2.dto.workexp.UpdateWorkExpDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.WorkExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/work-exp")
public class WorkExpController {

    @Autowired
    private WorkExpService workExpService;

    @PostMapping("/mobile/add")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> createWork(@RequestBody AddWorkExpDTO addWorkExpDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workExpService.addWorkExp(addWorkExpDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PutMapping("/mobile/update")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> updateWorkExp(@RequestBody UpdateWorkExpDTO updateWorkExpDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workExpService.updateWorkExp(updateWorkExpDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/mobile/remove/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> removeWorkExp(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workExpService.removeWorkExp(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/mobile/all-work-exp/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getAllWorkExp(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workExpService.getAllWorkExp(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/mobile/work-exp/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getWorkExp(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(workExpService.getWorkExp(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
