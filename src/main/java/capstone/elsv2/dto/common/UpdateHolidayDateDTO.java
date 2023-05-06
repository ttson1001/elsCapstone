package capstone.elsv2.dto.common;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHolidayDateDTO {
    @NotNull(message = "id không được để null")
    private String id;
    @NotNull(message = "startDate không được để null")
    private String startDate;
    @NotNull(message = "Endate không được để null")
    private String endDate;
    @NotNull(message = "Name không được null")
    @NotBlank(message = "Name không được để trống")
    private String name;
}
