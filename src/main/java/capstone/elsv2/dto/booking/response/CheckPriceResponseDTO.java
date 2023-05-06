package capstone.elsv2.dto.booking.response;

import capstone.elsv2.emunCode.PriceFactor;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckPriceResponseDTO {
    private HashMap<PriceFactor,DataDTO> datePriceInform;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private BigDecimal afterDiscountPrice;

    @Data
    @Builder
    public static class DataDTO{
        private int date;
        private BigDecimal percent;
        private BigDecimal price;
    }
}
