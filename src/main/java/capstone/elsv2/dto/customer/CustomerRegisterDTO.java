package capstone.elsv2.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegisterDTO {
    private String name;
    private String phone;
    private String email;
    private String password;
}
