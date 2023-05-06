package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.notification.MyNotificationDTO;
import capstone.elsv2.dto.notification.NotificationResponseDTO;
import capstone.elsv2.entities.Account;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableAsync
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private AccountRepository accountRepository;
    private String uri = "https://fcm.googleapis.com/fcm/send";
    private String key = "Authorization";
    private String value = "key=AAAA6a4FJNE:APA91bEPPATWeiBTpVjzf81pSGciIajVjC9uKGzcfOTP1HPu3xqH7ZzrqUeT9gp9kuAdh9Gi_uwk_vJK1b_oFYpd2MLMa254BB_mW6tR11VmnVBtRg5lMxLgXBe2n_dUQlmrQpJ9grCZ";
    @Override
    public NotificationResponseDTO sendNotification(String userId, String title, String body) {
        RestTemplate restTemplate = new RestTemplate();
        NotificationResponseDTO responseDTO = null;
        try {
            Account user = accountRepository.findById(userId).get();
            MyNotificationDTO myNotificationDTO = MyNotificationDTO.builder()
                    .title(title)
                    .body(body)
                    .build();
            responseDTO = NotificationResponseDTO.builder()
                    .to(user.getDeviceId())
                    .notification(myNotificationDTO)
                    .build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(key,value);
            HttpEntity<NotificationResponseDTO> request = new HttpEntity<>(responseDTO,httpHeaders);
            restTemplate.postForObject(uri,request,NotificationResponseDTO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseDTO;
    }

    @Override
    @Async
    public NotificationResponseDTO sendNotificationWithLink(String userId, String title, String body,String link) {
        RestTemplate restTemplate = new RestTemplate();
        NotificationResponseDTO responseDTO = null;
        try {
            Account user = accountRepository.findById(userId).get();
            MyNotificationDTO myNotificationDTO = MyNotificationDTO.builder()
                    .title(title)
                    .body(body)
                    .link(link)
                    .build();
            responseDTO = NotificationResponseDTO.builder()
                    .to(user.getDeviceId())
                    .notification(myNotificationDTO)
                    .build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(key,value);
            HttpEntity<NotificationResponseDTO> request = new HttpEntity<>(responseDTO,httpHeaders);
            restTemplate.postForObject(uri,request,NotificationResponseDTO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseDTO;
    }

}
