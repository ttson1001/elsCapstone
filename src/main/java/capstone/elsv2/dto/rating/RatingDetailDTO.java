package capstone.elsv2.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDetailDTO {
    private Integer countOneRate;
    private Integer countTwoRate;
    private Integer countThreeRate;
    private Integer countFourRate;
    private Integer countFiveRate;
    private String hasTag;
    private Float average;

}
