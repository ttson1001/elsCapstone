package capstone.elsv2.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTransactionDTO {
    private String type;
    private String paymentMethod;
    private String bookingId;

}
