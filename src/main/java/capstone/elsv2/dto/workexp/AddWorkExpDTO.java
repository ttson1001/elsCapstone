package capstone.elsv2.dto.workexp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddWorkExpDTO {
    private String sitterId;
    private String name;
    private String description;
    private String expTime;
}
