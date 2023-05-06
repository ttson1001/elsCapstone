package capstone.elsv2.services;

import capstone.elsv2.dto.rating.AddRatingDTO;
import capstone.elsv2.dto.rating.RatingDTO;
import capstone.elsv2.dto.rating.RatingDetailDTO;

import java.util.List;

public interface RatingService {
    Boolean addRating(AddRatingDTO addRatingDTO);

    List<RatingDTO> getAllRatingBySitterId(String sitterId);

    RatingDetailDTO getRatingBySitterId(String sitterId);


}
