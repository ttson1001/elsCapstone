package capstone.elsv2.dto.booking;

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
public class AddBookingDTO {
    private String address;
//    private String place;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String description;
//    private BigDecimal totalPrice;
    private String elderId;
    private String customerId;
    private String packageId;
    private List<AddBookingDetailDTO> addBookingDetailDTOS;
}
