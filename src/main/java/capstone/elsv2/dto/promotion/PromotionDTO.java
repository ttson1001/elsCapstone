package capstone.elsv2.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PromotionDTO {
    private String id;
    private String name;
    private String description;
    private String image;
    private Float value;
    private LocalDate startDate;
    private LocalDate endDate;
    private String code;
    private String status;
}
