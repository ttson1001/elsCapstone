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
public class UpdateCustomerDTO {
    private String id;
    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
    private String avatarImgUrl;
    private String latitude;
    private String longitude;
    private String district;
    private String description;
    private String idCardNumber;
}
