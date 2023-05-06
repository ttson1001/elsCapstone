package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;

import capstone.elsv2.dto.common.SearchDTO;
import capstone.elsv2.dto.customer.CustomerRegisterDTO;
import capstone.elsv2.dto.customer.UpdateCustomerDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.CusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    @Autowired
    CusService cusService;

    @GetMapping("admin/customers/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = cusService.findAllCustomer(pageNumber, pageSize);
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PostMapping("admin/customers-by-key-word")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> getAllByKeyWord(@RequestBody SearchDTO searchDTO) {
        PageDTO pageDTO = cusService.searchByKeyWord(searchDTO.getKeyWord(), searchDTO.getPageNumber(), searchDTO.getPageSize());
        pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(pageDTO);
    }


    @GetMapping("common/customer/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cusService.findById(id));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    @PutMapping("mobile/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateCustomer(@RequestBody UpdateCustomerDTO updateCustomerDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cusService.updateCustomer(updateCustomerDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/ban/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> banCustomer(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cusService.banCustomer(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("admin/unban/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> unBanCustomer(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cusService.unBanCustomer(id));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);

    }

    @PostMapping("mobile/register")
    @PermitAll
    public ResponseEntity<ResponseDTO> register(@RequestBody CustomerRegisterDTO customerRegisterDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(cusService.register(customerRegisterDTO));
        responseDTO.setSuccessCode(SuccessCode.REGISTER_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("mobile/get-all-message-customer/{customerId}")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    public ResponseEntity<ResponseDTO> findAllMessageByCustomerId(@PathVariable String customerId) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(cusService.getAllMessageByCustomerId(customerId));
//        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
//        return ResponseEntity.ok().body(responseDTO);
//    }


}
