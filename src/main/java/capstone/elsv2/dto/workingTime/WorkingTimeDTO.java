package capstone.elsv2.dto.workingTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkingTimeDTO {
    private String id;
    private String dayOfWeek; // MON 1-2-3-5-6|TUE
    private String slots;
    private String status;

}
