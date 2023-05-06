package capstone.elsv2.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingDTO {
    private Float deposit;
    private Float commission;
    private Float midnight;
    private Float weekend;
    private Float holiday;
}
