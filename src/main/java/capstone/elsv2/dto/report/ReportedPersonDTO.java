package capstone.elsv2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportedPersonDTO {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String role;
    private String status;
}
