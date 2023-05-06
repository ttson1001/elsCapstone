package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateSitterContactDTO {
    private String sitterId;
    private String phone;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private String district;
}
