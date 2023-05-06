package capstone.elsv2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportDTO {
    private String id;
    private String title;
    private String reporter;
    private String reportedPerson;
    private LocalDateTime createDate;
    private String status;
}
