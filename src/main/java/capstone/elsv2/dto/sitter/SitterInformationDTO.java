package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SitterInformationDTO {
    private String sitterId;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String idNumber;
    private String avatarImg;
    private String backCardImg;
    private String frontCardImg;
    private String status;
}
