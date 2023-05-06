package capstone.elsv2.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddRatingDTO {
    private Float rate;
    private String comment;
    private String hagTag;
    private String bookingId;

}
