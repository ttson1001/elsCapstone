package capstone.elsv2.dto.booking;

import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.report.ReportDetailDTO;
import capstone.elsv2.dto.report.ReportHistoryDTO;
import capstone.elsv2.entities.DetailService;
import capstone.elsv2.entities.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailFormDTO {
    private String id;
    private Integer estimateTime;
    private String packageName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private PackageDTO packageDTO;
    private String status;
    private List<DetailServiceDTO> detailServiceDTOS;
    private List<ReportHistoryDTO> reportHistoryDTOS;
}
