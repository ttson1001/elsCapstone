package capstone.elsv2.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageParamDTO {
    private Integer pageNumber;
    private Integer pageSize;
}
