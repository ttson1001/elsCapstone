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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private String id;
    private String address;
//    private String place;
    private LocalDate createDate;
    private LocalDate startDate;
    private LocalTime endTime;
    private LocalTime startTime;
    private Double latitude;
    private Double longitude;
    private LocalDate endDate;
    private String status;
    private CustomerDTO customerDTO;
    private SitterDTO sitterDTO;
    private ElderDTO elderDTO;
    private BigDecimal totalPrice;

}
