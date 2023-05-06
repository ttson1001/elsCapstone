package capstone.elsv2.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingScheduleCustomerDTO {
    private String bookingId;
    private String bookingDetailId;
    private String sitterName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String bookingStatus;
    private String bookingDetailStatus;

}
