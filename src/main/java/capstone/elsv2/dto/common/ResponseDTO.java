package capstone.elsv2.dto.common;

import capstone.elsv2.emunCode.ErrorCode;
import capstone.elsv2.emunCode.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {
    private SuccessCode successCode;
    private ErrorCode errorCode;
    private  Object data;
}
