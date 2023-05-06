package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/zone")
public class ZoneController {
    @Autowired
    private ZoneService zoneService;
    @GetMapping("init")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO> initZone(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(zoneService.initZone());
        responseDTO.setSuccessCode(SuccessCode.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
