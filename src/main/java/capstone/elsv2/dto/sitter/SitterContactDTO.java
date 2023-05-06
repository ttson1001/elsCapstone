package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SitterContactDTO {
    private String sitterId;
    private String phone;
    private String email;
    private String address;

    private Double latitude;
    private Double longitude;
    private String description;
    private String district;
}
