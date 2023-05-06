package capstone.elsv2.dto.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTrackingDTO {
    private String bookingDetailId;
    private String image;
    private String note;
}
