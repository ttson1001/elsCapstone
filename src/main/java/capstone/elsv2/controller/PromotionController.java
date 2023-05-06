package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.common.SearchDTO;
import capstone.elsv2.dto.promotion.AddPromotionDTO;
import capstone.elsv2.dto.promotion.UpdatePromotionDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @PostMapping("admin/add")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> addPromotion(@RequestBody @Valid AddPromotionDTO addPromotionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.addPromotion(addPromotionDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PostMapping("admin/promotions")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getALLPromotion(@RequestBody SearchDTO searchDTO) {
        PageDTO pageDTO = promotionService.getAllPromotion(searchDTO.getKeyWord(), searchDTO.getPageNumber(), searchDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("mobile/promotions")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getALLPromotion() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getAllPromotion());
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-by-id/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getPromotionMobile(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getPromotion(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getPromotion(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getPromotion(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> updatePromotion(@RequestBody @Valid UpdatePromotionDTO promotionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.updatePromotion(promotionDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/activate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> activePromotion(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.activePromotion(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/deactivate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> deActivePromotion(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.deActivePromotion(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/gets-promotion-id/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getPromotionByUser(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(promotionService.getAllPromotion(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
