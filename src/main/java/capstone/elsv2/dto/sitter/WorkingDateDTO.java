package capstone.elsv2.dto.sitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkingDateDTO {
    private List<String> workingDate;
    private Boolean isDate;
    private Boolean isWeek;
    private Boolean isMonth;
}
