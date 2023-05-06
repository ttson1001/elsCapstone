package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SitterPriorityDTO {
    private String sitterId;
    private double priority;
    private int count;
}
