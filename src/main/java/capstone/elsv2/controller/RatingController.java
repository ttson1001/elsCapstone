package capstone.elsv2.controller;

import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.rating.AddRatingDTO;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @PostMapping("mobile/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> payBooking(@RequestBody AddRatingDTO addRatingDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(ratingService.addRating(addRatingDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-rating-by-sitter/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getAllRatingBySitter(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(ratingService.getAllRatingBySitterId(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-rating-by-sitter/{sitterId}")
    @PreAuthorize("hasRole('SITTER')")
    public ResponseEntity<ResponseDTO> getRatingBySitter(@PathVariable String sitterId){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(ratingService.getRatingBySitterId(sitterId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
