package capstone.elsv2.dto.booking.response;

import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.elder.ElderDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.sitter.SitterDTO;
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
public class BookingResponseDTO {
    private String id;
    private String address;
    private String place;
    private String startDate;
    private String slots;
    private String endDate;
    private String description;
    private ElderDTO elder;
    private SitterDTO sitter;
    private String status;
    private BigDecimal deposit;
    private Integer numOfChange;
    private String district;
    private List<BookingDetailResponseDTO> bookingDetails;
}
