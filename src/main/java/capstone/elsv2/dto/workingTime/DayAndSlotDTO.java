package capstone.elsv2.dto.workingTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DayAndSlotDTO {
    private String slot;
    private String day;
}
