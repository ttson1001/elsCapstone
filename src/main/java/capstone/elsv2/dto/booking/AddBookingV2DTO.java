package capstone.elsv2.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBookingV2DTO {
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String description;
    private String elderId;
    private Double latitude;
    private Double longitude;
    private String customerId;
    private String packageId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> dates;
    private LocalTime startTime;
    private String promotion;
    private String district;
}
