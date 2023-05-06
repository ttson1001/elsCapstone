package capstone.elsv2.dto.mypackage;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddPackageDTO {
    private String name;
    private String img;
    private String healthStatus;
    private String slotStart;
    private String slotEnd;
    private String desc;
    private List<String> serviceId;
}
