package capstone.elsv2.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderChatDTO {
    private String otherId;
    private String otherName;
    private String otherEmail;
    private String otherAvatar;
}
