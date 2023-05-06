package capstone.elsv2.dto.mypackage;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PackageDTO {
    private String id;
    private String name;
    private String img;
    private Integer startSlot;
    private Integer endSlot;
    private Integer duration;
    private BigDecimal price;
    private String desc;
    private String status;
}
