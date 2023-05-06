package capstone.elsv2.dto.location;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CityDTO {
    private String name;
    private Integer code;
    private String codename;
    private String division_type;
    private Integer phone_code;
}
