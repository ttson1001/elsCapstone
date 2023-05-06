package capstone.elsv2.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RatingDTO {

    private String id;
    private Float rate;
    private String comment;
    private String bookingId;
}
