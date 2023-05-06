package capstone.elsv2.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffAccountDTO {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String avatarImage;
    private LocalDate dob;
}
