package capstone.elsv2.dto.service;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddServiceDTO {
    private String name;
    private BigDecimal price;
    private float duration;
    private String imgUrl;
}
