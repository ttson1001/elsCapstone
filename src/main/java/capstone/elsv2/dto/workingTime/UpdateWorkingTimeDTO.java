package capstone.elsv2.dto.workingTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateWorkingTimeDTO {
    private String id;
    private Boolean isDate;   //true
    private Boolean isWeek; // true
//    private String dayOfWeek; // MON 1-2-3-5-6|TUE
    private String slots;
}
