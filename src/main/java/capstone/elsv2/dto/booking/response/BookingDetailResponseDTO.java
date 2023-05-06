package capstone.elsv2.dto.booking.response;

import capstone.elsv2.dto.service.ServiceDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponseDTO {
    private String startDateTime;  //yyyy-MM-dd HH:mm
    private String endDateTime;
    private String packageName;
    private BigDecimal percentChange;
    private BigDecimal price;
    private String status;
}
