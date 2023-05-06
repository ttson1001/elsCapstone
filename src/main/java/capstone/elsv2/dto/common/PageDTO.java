package capstone.elsv2.dto.common;

import capstone.elsv2.emunCode.ErrorCode;
import capstone.elsv2.emunCode.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO {
    private SuccessCode successCode;
    private ErrorCode errorCode;
    private Object data;
    private Integer totalPages;
    private Integer pageIndex;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
    private Integer pageSize;

    private Long totalRecord;
    private Integer fromRecord;
    private Integer toRecord;
}
