package capstone.elsv2.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePromotionDTO {
    @NotNull(message = "id không được bỏ trống")
    @NotBlank(message = "id không được bỏ trống")
    private String id;
    @NotNull(message = "name không được bỏ trống")
    @NotBlank(message = "name không được bỏ trống")
    private String name;
    private String description;
    @NotNull(message = "image không được bỏ trống")
    @NotBlank(message = "image không được bỏ trống")
    private String image;
    @NotNull(message = "value không được bỏ trống")
    private Float value;
    @NotNull(message = "startDate không được bỏ trống")
    @NotBlank(message = "startDate không được bỏ trống")
    private String startDate;
    @NotNull(message = "endDate không được bỏ trống")
    @NotBlank(message = "endDate không được bỏ trống")
    private String endDate;
}
