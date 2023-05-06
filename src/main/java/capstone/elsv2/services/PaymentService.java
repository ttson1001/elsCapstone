package capstone.elsv2.services;

import capstone.elsv2.dto.booking.AddBookingV2DTO;
import capstone.elsv2.dto.payment.MomoClientRequest;
import capstone.elsv2.dto.payment.MomoResponse;
import capstone.elsv2.dto.transaction.AddTransactionDTO;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    Boolean payBooking(AddTransactionDTO addTransactionDTO);

    ResponseEntity<MomoResponse> payBookingOnline(AddBookingV2DTO addBookingV2DTO);

    ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request); ////
}
