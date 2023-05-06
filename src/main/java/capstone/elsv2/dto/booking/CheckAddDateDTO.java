package capstone.elsv2.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckAddDateDTO {
    private Boolean isAdd;
    private List<String> dates;
    private String maxDate;
}
