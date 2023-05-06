package capstone.elsv2.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginGmailDTO {
    @Email(message = "vui lòng nhập đúng email")
    private String email;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String token;
}
