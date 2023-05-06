package capstone.elsv2.dto.workexp;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateWorkExpDTO {
    private String id;
    private String name;
    private String description;
    private String expTime;
}
