package capstone.elsv2.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelPersonDTO {
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String image;
    private String status;
    private String roleName;
}
