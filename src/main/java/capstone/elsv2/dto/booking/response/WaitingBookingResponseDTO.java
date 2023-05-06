package capstone.elsv2.dto.booking.response;

import capstone.elsv2.dto.elder.ElderDTO;
import capstone.elsv2.entities.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingBookingResponseDTO {
    private String bookingId;
    private String address;
    private LocalDate startDate;
    private String slots;
    private LocalDate endDate;
    private String description;
    private ElderDTO elder;
    private BigDecimal totalPrice;
    private Package aPackage;
}
