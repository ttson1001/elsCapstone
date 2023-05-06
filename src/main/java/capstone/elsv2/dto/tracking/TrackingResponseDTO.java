package capstone.elsv2.dto.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingResponseDTO {
    private String bookingDetailId;
    private String date;
    private List<TrackingDTO> trackingDTOList;
}
