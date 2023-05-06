package capstone.elsv2.dto.promotion;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddPromotionDTO {
    @NotNull(message = "Name không được bỏ trống")
    @NotBlank(message = "Name không được bỏ trống")
    private String name;
    private String description;
    @NotNull(message = "value không được bỏ trống")
    private Float value;
    @NotNull(message = "image không được bỏ trống")
    @NotBlank(message = "image không được bỏ trống")
    private String image;
    @NotNull(message = "startDate không được bỏ trống")
    @NotBlank(message = "startDate không được bỏ trống")
    private String startDate;
    @NotNull(message = "endDate không được bỏ trống")
    @NotBlank(message = "endDate không được bỏ trống")
    private String endDate;
}
