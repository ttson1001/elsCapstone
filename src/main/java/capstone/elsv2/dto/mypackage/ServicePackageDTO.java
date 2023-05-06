package capstone.elsv2.dto.mypackage;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServicePackageDTO {
    private String packageId;
    private List<String> serviceId;
}
