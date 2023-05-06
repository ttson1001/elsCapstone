package capstone.elsv2.controller;

import capstone.elsv2.dto.certificate.CertificateDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.sitter.SitterCertificateDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("mobile/create")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> addCertificate(@RequestBody SitterCertificateDTO sitterCertificateDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(certificateService.addCertificate(sitterCertificateDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("common/all/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER','ADMIN')")
    public ResponseEntity<ResponseDTO> getAllCertificateBySitterId(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(certificateService.getAllCertificateBySitterId(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("common/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getCertificate(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(certificateService.getCertificate(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("mobile/update")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> updateCertificate(@RequestBody CertificateDTO certificateDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(certificateService.updateCertificate(certificateDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("mobile/remove/{id}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> removeCertificate(@PathVariable String id){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(certificateService.removeCertificate(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);

    }







}
