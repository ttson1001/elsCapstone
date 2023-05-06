package capstone.elsv2.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {
    private String id;
    private String fullName;
    private String phone;
    private Integer age;
    private String email;//
    private String address;
    private String gender;
    private String image;
    private String status;
}
