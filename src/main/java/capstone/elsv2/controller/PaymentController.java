package capstone.elsv2.controller;

import capstone.elsv2.dto.booking.AddBookingV2DTO;
import capstone.elsv2.dto.common.Common;
import capstone.elsv2.dto.payment.*;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.transaction.AddTransactionDTO;
import capstone.elsv2.emunCode.PaymentMethod;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.WalletRepository;
import capstone.elsv2.repositories.WalletTransactionRepository;
import capstone.elsv2.sercurity.Utilities;
import capstone.elsv2.services.AssigningService;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.services.PaymentService;
import capstone.elsv2.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private AssigningService assigningService;
    @Autowired
    private WalletRepository walletRepository;
    private final ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("mobile/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> payBooking(@RequestBody AddTransactionDTO addTransactionDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(paymentService.payBooking(addTransactionDTO));
        responseDTO.setSuccessCode(SuccessCode.PAY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/pay-booking")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> payOnline(@RequestBody AddBookingV2DTO addBookingV2DTO) {
        ResponseEntity<MomoResponse> response = null;
        response = paymentService.payBookingOnline(addBookingV2DTO);
        return ResponseEntity.ok().body(response.getBody().getPayUrl());
    }


    // chỉnh lại nguồn tiền

    @PostMapping("mobile/top-up")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> topUpToWallet(@RequestBody AddWalletDTO addWalletDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(walletService.addWallet(addWalletDTO));
        responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/top-up-v2")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<String> topUpToWalletV2(@RequestBody AddWalletDTO addWalletDTO) {
        ResponseEntity<MomoResponse> response = null;
        response = walletService.addWalletV2(addWalletDTO);
        return ResponseEntity.ok().body(response.getBody().getPayUrl());
    }

    @GetMapping("common/get-wallet/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','SITTER')")
    public ResponseEntity<ResponseDTO> getWallet(@PathVariable String userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(walletService.getWallet(userId));
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("admin/get-wallet-system")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> getWalletSystem() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(walletService.getWalletSystem());
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("admin/refund")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> refund(@RequestBody RefundDTO refundDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(walletService.refund(refundDTO));
        responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-transaction/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getAllTransaction(@PathVariable String userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(walletService.getAllTransaction(userId));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("mobile/momo")
    @PermitAll
    public ResponseEntity<MomoResponse> paymentWithMomo(@RequestBody @Valid MomoClientRequest request) {
        return paymentService.getPaymentMomo(request);
    }

    @GetMapping("mobile/MomoConfirm/{type}/{userId}/{address}/{startDate}/{endDate}/{description}/{elderId}/{latitude}/{longitude}/{packageId}/{dates}/{startTime}/{promotion}/{district}")
    @PermitAll
    public ResponseEntity<MomoConfirmResultResponse> momoConfirm(@PathVariable String type,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String address,
                                                                 @PathVariable String startDate,
                                                                 @PathVariable String endDate,
                                                                 @PathVariable String description,
                                                                 @PathVariable String elderId,
                                                                 @PathVariable String latitude,
                                                                 @PathVariable String longitude,
                                                                 @PathVariable String packageId,
                                                                 @PathVariable String dates,
                                                                 @PathVariable String startTime,
                                                                 @PathVariable String promotion,
                                                                 @PathVariable String district,
                                                                 @RequestParam("partnerCode") String partnerCode,
                                                                 @RequestParam("orderId") String orderId,
                                                                 @RequestParam("requestId") String requestId,
                                                                 @RequestParam("amount") long amount,
                                                                 @RequestParam("orderInfo") String orderInfo,
                                                                 @RequestParam("orderType") String orderType,
                                                                 @RequestParam("transId") long transId,
                                                                 @RequestParam("resultCode") int resultCode,
                                                                 @RequestParam("message") String message,
                                                                 @RequestParam("payType") String payType,
                                                                 @RequestParam("responseTime") String responseTime,
                                                                 @RequestParam("extraData") String extraData,
                                                                 @RequestParam("signature") String signature) {
        String sign = "accessKey=" +
                Common.ACCESS_KEY + "&orderId=" + orderId + "&partnerCode=" + Common.PARTNER_CODE
                + "&requestId=" + requestId;
        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        MomoConfirmResultResponse momoConfirmResultResponse = new MomoConfirmResultResponse(
                partnerCode, orderInfo, responseTime, amount, orderInfo, orderType, transId,
                resultCode, message, payType, resultCode, extraData, signatureHmac, Common.PARTNER_CODE);

        System.out.println(type);
        String msg = "";
        if (resultCode == 0) {
            // System.out.println("Giao Dich Thanh cong");
            msg = "giao dich thanh cong";
        } else if (resultCode == 9000) {
            msg = "giao dich duoc xac nhan, giao dich thang cong!";
            if (type.equals(TypeCode.TOP_UP.toString())) {
                AddWalletDTO addWalletDTO = AddWalletDTO.builder()
                        .customerId(userId)
                        .amount(BigDecimal.valueOf(amount))
                        .build();
                walletService.addWallet(addWalletDTO);
            } else {
                try {
                    String d = dates;
                    List<LocalDate> dates1 = new ArrayList<>();
                    String[] date = d.split(" ");
                    for (int i = 0; i < date.length; i++) {
                        dates1.add(LocalDate.parse(date[i]));
                    }
                    AddBookingV2DTO addBookingV2DTO = AddBookingV2DTO.builder()
                            .address(address)
                            .startDate(LocalDate.parse(startDate))
                            .endDate(LocalDate.parse(endDate))
                            .description(description)
                            .elderId(elderId)
                            .latitude(Double.parseDouble(latitude))
                            .longitude(Double.parseDouble(longitude))
                            .customerId(userId)
                            .packageId(packageId)
                            .dates(dates1)
                            .startTime(LocalTime.parse(startTime))
                            .district(district)
                            .build();
                    if (promotion.equals("null")|| promotion == null) addBookingV2DTO.setPromotion(null);
                    else addBookingV2DTO.setPromotion(promotion);

                    Booking booking = bookingService.bookingWithoutWallet(addBookingV2DTO);

                    //assign


                    System.out.println("Thành công");

                    Wallet customerWallet = walletRepository.findById(userId).get();
                    Transaction walletTransaction = Transaction.builder()
                            .type(TypeCode.DEPOSIT.toString())
                            .createDateTime(LocalDateTime.now(istZoneId))
                            .wallet(customerWallet)
                            .amount(BigDecimal.valueOf(amount))
                            .paymentMethod(PaymentMethod.MOMO.toString())
                            .booking(booking)
                            .build();
                    walletTransactionRepository.save(walletTransaction);
                    Account admin = accountRepository.findByRole_Name("ADMIN");
                    Wallet adminWallet = walletRepository.findById(admin.getId()).get();
                    adminWallet.setAmount(adminWallet.getAmount().add(BigDecimal.valueOf(amount)));
                    walletRepository.save(adminWallet);

                    Transaction walletTransactionAdmin = Transaction.builder()
                            .type(TypeCode.DEPOSIT.toString())
                            .createDateTime(LocalDateTime.now(istZoneId))
                            .wallet(adminWallet)
                            .amount(BigDecimal.valueOf(amount))
                            .paymentMethod(PaymentMethod.MOMO.toString())
                            .booking(booking)
                            .build();
                    walletTransactionRepository.save(walletTransactionAdmin);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create("https://elscusv2.page.link/success?id="+booking.getId()));
                    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

                }catch (Exception e){
                    Wallet customerWallet = walletRepository.findById(userId).get();
                    customerWallet.setAmount(customerWallet.getAmount().add(BigDecimal.valueOf(amount)));
                    Transaction walletTransaction = Transaction.builder()
                            .type(TypeCode.REFUND.toString())
                            .createDateTime(LocalDateTime.now(istZoneId))
                            .wallet(customerWallet)
                            .amount(BigDecimal.valueOf(amount))
                            .paymentMethod(PaymentMethod.MOMO.toString())
                            .build();
                    walletTransactionRepository.save(walletTransaction);
                    e.printStackTrace();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create("https://elscusv2.page.link/fail"));
                    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
                }
            }

            String[] listOrderId = orderId.split("-");

        }
        if (resultCode == 1006) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("https://elscusv2.page.link/fail"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        System.out.println(resultCode);
        System.out.println(msg);
        // accessKey=WehkypIRwPP14mHb&orderId=23&partnerCode=MOMODJMX20220717&requestId=48468005-6de1-4140-839f-5f2d8d77a001
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://elscusv2.page.link/success"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }



}
