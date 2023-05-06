package capstone.elsv2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// sitter report customer
public class AddReportDTO {
    private String bookingDetailId;
    private String title;
    private String content;
//    private String image;
    private String sitterId;
    private String customerId;
}
