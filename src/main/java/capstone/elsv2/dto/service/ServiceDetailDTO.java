package capstone.elsv2.dto.service;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ServiceDetailDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private Float duration;
    private String imgUrl;
}
