package capstone.elsv2.dto.elder;

import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ElderDTO {
    private String id;
    private String fullName;
    private LocalDate dob;
    private Integer age;
    private String gender;
    private String healStatus;
}
