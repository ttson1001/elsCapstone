package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SitterFullInformationDTO {
    private String email;
    private String fullName;
    private String phone;
    private String gender;
    private String address;
    private LocalDate dob;

    // img
    private String avatarImgUrl;
    private String backCardImgUrl;
    private String frontCardImgUrl;

    // #
    private String status;
    private LocalDate createDate;
    private String tokenDevice;
    private String description;
    private String idCardNumber;


}
