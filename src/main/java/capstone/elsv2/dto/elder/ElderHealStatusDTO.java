package capstone.elsv2.dto.elder;

import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ElderHealStatusDTO {
    private String id;
    private String fullName;
    private LocalDate dob;
    private String healthStatus;
    private String gender;
}
