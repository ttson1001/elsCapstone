package capstone.elsv2.dto.elder;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ElderDetailDTO {
    private String fullName;
    private LocalDate dob;
    private String healthStatus;
    private String idCardNumber;
    private String note;
    private String gender;
}
