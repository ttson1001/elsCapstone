package capstone.elsv2.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {
    private Integer totalCustomer;
    private Integer totalSitter;
    private Integer totalBooking;
    private Integer totalElder;
}
