package capstone.elsv2.dto.mypackage;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdatePackageDTO {
    private String id;
    private String name;
    private String img;
    private String slotStart;
    private String slotEnd;
    private String healthStatus;
//    private BigDecimal price;
    private String desc;
}
