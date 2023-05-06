package capstone.elsv2.dto.service;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ServiceDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private String  status;
    private Float duration;
//    private String requirement;
}
