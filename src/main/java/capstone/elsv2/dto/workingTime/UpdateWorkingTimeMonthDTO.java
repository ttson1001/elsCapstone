package capstone.elsv2.dto.workingTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateWorkingTimeMonthDTO {
    private String id;
    private Boolean isMonth;
    private String slotWeekOne;
    private Boolean isWeekOne;
    private String slotWeekTwo;
    private Boolean isWeekTwo;
    private String slotWeekThree;
    private Boolean isWeekThree;
    private String slotWeekFour;
    private Boolean isWeekFour;
}
