package capstone.elsv2.dto.workingTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddWorkingTimeWeekDTO {
    private String sitterId;
//    private Boolean isWeek; // true
    private String dayOfWeekAndSlots; // MONDAY 1-2-3-5-6;TUESDAY 1-2-3-4
}
