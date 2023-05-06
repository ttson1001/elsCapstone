package capstone.elsv2.dto.education;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EducationDTO {
    private String id;
    private String educationLevel;
    private String schoolName;
    private Float gpa;
    private Boolean isGraduate;
}
