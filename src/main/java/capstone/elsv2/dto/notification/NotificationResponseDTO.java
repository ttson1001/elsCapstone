package capstone.elsv2.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationResponseDTO {
    private String to;
    private MyNotificationDTO notification;
    private MyData data;

}
