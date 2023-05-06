package capstone.elsv2.dto.workexp;

import lombok.*;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkExpDTO {

    private String id;

    private String name;

    private String description;

    private String expTime;

}
