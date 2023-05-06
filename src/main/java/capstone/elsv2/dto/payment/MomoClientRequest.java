package capstone.elsv2.dto.payment;


import capstone.elsv2.dto.booking.AddBookingV2DTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Builder
@Getter
public class MomoClientRequest {
      private String address;
      private String startDate;
      private String endDate;
      private String description;
      private String elderId;
      private String latitude;
      private String longitude;
      private String packageId;
      private String startTime;
      private String promotion;
      private String district;

      private String dates;
      ////
      private String bookingId;
      private String userId;
      private String type;
      private Long amount;
      private String orderId;
      private String deeplink;


      

}
