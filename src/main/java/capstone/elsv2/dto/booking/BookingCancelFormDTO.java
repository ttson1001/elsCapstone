package capstone.elsv2.dto.booking;

import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.elder.ElderDTO;
import capstone.elsv2.dto.sitter.SitterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCancelFormDTO {
    private String id;
    private String address;
//    private String place;
    private LocalDate createDate;
    private LocalDate cancelDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal deposit;
    private String status;
    private String reason;
    private BigDecimal totalPrice;
    private CustomerDTO customerDTO;
    private ElderDTO elderDTO;
    private SitterDTO sitterDTO;
    private CancelPersonDTO cancelPersonDTO;
    private List<BookingDetailFormDTO> bookingDetailFormDTOS;
}
