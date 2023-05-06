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
public class SitterFormDTO {

    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String status;
    private LocalDate createDate;

}
