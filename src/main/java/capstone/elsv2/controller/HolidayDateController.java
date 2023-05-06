package capstone.elsv2.controller;

import capstone.elsv2.dto.common.AddHolidayDateDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.common.UpdateHolidayDateDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.HolidayDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/holiday")
public class HolidayDateController {
    @Autowired
    HolidayDateService holidayDateService;

    @PostMapping("admin/add")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> addHolidayDate(@RequestBody @Valid AddHolidayDateDTO addHolidayDateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(holidayDateService.addHoliday(addHolidayDateDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-all")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getAllHolidayDate() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(holidayDateService.getAllHoliday());
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> updateHoliday(@RequestBody @Valid UpdateHolidayDateDTO updateHolidayDateDTO,  BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,bindingResult.getFieldError().getDefaultMessage());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(holidayDateService.updateHoliday(updateHolidayDateDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
