package capstone.elsv2.dto.booking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptBookingRequestDTO {
    private Boolean isAccept;
    private String bookingId;
    private String sitterId;
}
