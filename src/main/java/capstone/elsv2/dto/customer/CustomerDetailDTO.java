package capstone.elsv2.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String status;
    private LocalDate dob;
    private String avatar;
    private String zone;
    private Double longitude;
    private Double latitude;
    private LocalDate createDate;
    private String description;
    private String idCardNumber;

}
