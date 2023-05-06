package capstone.elsv2.services;

import capstone.elsv2.dto.notification.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO sendNotification(String userId, String title, String body);
    NotificationResponseDTO sendNotificationWithLink(String userId, String title, String body,String link);
}
