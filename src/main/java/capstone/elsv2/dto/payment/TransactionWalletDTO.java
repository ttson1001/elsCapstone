package capstone.elsv2.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionWalletDTO {
    private String id;
    private String type;
    private String amount;
    private String content;
    private LocalDateTime createDateTime;
}
