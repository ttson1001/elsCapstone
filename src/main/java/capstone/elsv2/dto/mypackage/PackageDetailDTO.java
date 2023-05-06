package capstone.elsv2.dto.mypackage;

import capstone.elsv2.dto.service.ServiceDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PackageDetailDTO {
    private String id;
    private String name;
    private String img;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotStart;
    private Integer slotEnd;
    private Integer duration;
    private BigDecimal price;
    private String healthStatus;
    private String desc;
    private List<ServiceDTO> serviceDTOS;
}
