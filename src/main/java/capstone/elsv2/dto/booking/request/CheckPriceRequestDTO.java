package capstone.elsv2.dto.booking.request;

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
public class CheckPriceRequestDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> date;
    private String packageId;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    private String promotion;
}
