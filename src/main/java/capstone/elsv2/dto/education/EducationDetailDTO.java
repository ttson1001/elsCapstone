package capstone.elsv2.dto.education;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EducationDetailDTO {
    private String id;
    private String educationLevel;
    private String major;
    private String schoolName;
    private LocalDate fromDate;
    private LocalDate endDate;
    private Boolean isGraduate;
    private Float GPA;
    private String educationImg;
    private String description;

}
