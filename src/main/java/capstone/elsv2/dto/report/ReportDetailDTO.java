package capstone.elsv2.dto.report;

import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.sitter.SitterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportDetailDTO {
    private String id;
    private String title;
    private ReporterDTO reporterDTO;
    private ReportedPersonDTO reportedPersonDTO;
    private String content;
    private LocalDateTime createDate;
    private String status;
}
