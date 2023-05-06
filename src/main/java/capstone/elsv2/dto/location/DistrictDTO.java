package capstone.elsv2.dto.location;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DistrictDTO {
    private String name;
    private Integer code;
    private String codename;
    private String division_type;
    private int province_code;
}
