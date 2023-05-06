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
public class SitterDTO {
    private String id;
    private String fullName;
    private Integer age;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String image;
    private Float rate;
    private String reason;
    private String status;
}
