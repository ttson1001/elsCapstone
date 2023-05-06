package capstone.elsv2.dto.sitterpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSitterPackageDTO {
    private String sitterId;
    private List<String> packageIds;
}
