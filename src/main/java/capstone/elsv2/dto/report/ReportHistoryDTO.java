package capstone.elsv2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportHistoryDTO {
    private String id;
    private String title;
    private String sitterName;
    private String customerName;
    private String content;
    private String reply;
    private LocalDateTime createDate;
    private String status;
}

