package capstone.elsv2.dto.common;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddHolidayDateDTO {
    @NotNull(message = "startDate không được để null")
    private LocalDate startDate;
    @NotNull(message = "Endate không được để null")
    private LocalDate endDate;
    @NotNull(message = "Name không được null")
    @NotBlank(message = "Name không được để trống")
    private String name;
}
