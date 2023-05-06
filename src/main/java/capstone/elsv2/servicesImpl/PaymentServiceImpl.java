package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.booking.AddBookingV2DTO;
import capstone.elsv2.dto.booking.request.CheckPriceRequestDTO;
import capstone.elsv2.dto.common.Common;
import capstone.elsv2.dto.payment.MomoClientRequest;
import capstone.elsv2.dto.payment.MomoRequest;
import capstone.elsv2.dto.payment.MomoResponse;
import capstone.elsv2.sercurity.Utilities;
import capstone.elsv2.dto.transaction.AddTransactionDTO;
import capstone.elsv2.emunCode.PaymentMethod;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerProfileRepository customerProfileRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    BookingService bookingService;

    @Autowired
    PaymentService paymentService;
    @Autowired
    private ReportRepository reportRepository;

    @Override
    @Transactional
    public Boolean payBooking(AddTransactionDTO addTransactionDTO) {
        Booking booking = bookingRepository.findById(addTransactionDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy booking nào"));
        CustomerProfile customerProfile = customerProfileRepository.findById(booking.getCustomer().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy khách hàng nào"));
        Account admin = accountRepository.findByRole_Name("ADMIN");
        Setting setting = settingRepository.findById("1").get();

        // tinh chiet khấu
        BigDecimal realPay = booking.getTotalPrice().subtract(booking.getDeposit());
        BigDecimal deposit = booking.getDeposit();
        BigDecimal commission = booking.getTotalPrice().multiply(BigDecimal.valueOf(setting.getCommission())); // 20%
        BigDecimal sitterEarn = (deposit.subtract(commission)).add(realPay);

        if (addTransactionDTO.getPaymentMethod().equals(PaymentMethod.WALLET.toString())) {
            Wallet customerWallet = walletRepository.findById(booking.getCustomer().getId()).get();
            if (customerWallet.getAmount().compareTo(realPay) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "Tài khoản trong ví của bạn khg đủ để thực hiện giao dịch vui lòng nạp thêm");
            } else {
                customerWallet.setAmount(customerWallet.getAmount().subtract(realPay));
                walletRepository.save(customerWallet);
                Transaction walletTransactionCus = Transaction.builder()
                        .type(TypeCode.PAY_BOOKING.toString())
                        .createDateTime(LocalDateTime.now())
                        .wallet(customerWallet)
                        .amount(realPay)
                        .paymentMethod(PaymentMethod.WALLET.toString())
                        .build();
                walletTransactionRepository.save(walletTransactionCus);
                Wallet sitterWallet = walletRepository.findById(booking.getSitter().getId()).get();
                sitterWallet.setAmount(sitterWallet.getAmount().add(sitterEarn));
                walletRepository.save(sitterWallet);
                Transaction walletTransactionSit = Transaction.builder()
                        .type(TypeCode.RECEIVE.toString())
                        .createDateTime(LocalDateTime.now())
                        .wallet(sitterWallet)
                        .amount(sitterEarn)
                        .paymentMethod(PaymentMethod.WALLET.toString())
                        .build();
                walletTransactionRepository.save(walletTransactionSit);
                Wallet adminWallet = walletRepository.findById(admin.getId()).get();
                adminWallet.setAmount(adminWallet.getAmount().subtract(deposit.subtract(commission)));
                walletRepository.save(adminWallet);
                Transaction walletTransactionAdmin = Transaction.builder()
                        .type(TypeCode.REVENUE.toString())
                        .createDateTime(LocalDateTime.now())
                        .wallet(adminWallet)
                        .amount(deposit.subtract(deposit.subtract(commission)))
                        .paymentMethod(PaymentMethod.WALLET.toString())
                        .build();
                walletTransactionRepository.save(walletTransactionAdmin);
            }
        } else {
            Wallet sitterWallet = walletRepository.findById(booking.getSitter().getId()).get();
            sitterWallet.setAmount(sitterWallet.getAmount().add(sitterEarn));
            walletRepository.save(sitterWallet);
            Wallet adminWallet = walletRepository.findById(admin.getId()).get();
            adminWallet.setAmount(adminWallet.getAmount().subtract(deposit.subtract(commission)));
            walletRepository.save(adminWallet);
        }
        try {
            booking.setStatus(StatusCode.PAID.toString());
            bookingRepository.save(booking);


        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Thanh toán không thàng công xin vui lòng thử lại");
        }
        return true;
    }

    @Override
    public ResponseEntity<MomoResponse> getPaymentMomo(MomoClientRequest request) {

        // request url
        String url = Common.MOMO_URI;

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object

        // build the request
        MomoRequest momoReq = new MomoRequest();
        // CustomerInfoMomoRequest customerInfo = new CustomerInfoMomoRequest("dat",
        // "0123456789", "dat@gmail.com");

        DecimalFormat df = new DecimalFormat("#");

        String amount = String.valueOf(df.format(request.getAmount()));

        String sign = "accessKey=" + Common.ACCESS_KEY + "&amount=" + amount + "&extraData="
                + "&ipnUrl=" + Common.IPN_URL_MOMO + "&orderId=" + request.getOrderId() + "&orderInfo="
                + "Thanh toan momo"
                + "&partnerCode=" + Common.PARTNER_CODE + "&redirectUrl=" + Common.REDIRECT_URL_MOMO + request.getType() + "/" + request.getUserId() +"/" + request.getAddress() +"/"+request.getStartDate()+"/"+request.getEndDate()+"/"+request.getDescription()+"/"+request.getElderId()+"/"+request.getLatitude()+"/"+request.getLongitude()+"/"+request.getPackageId()+"/"+request.getDates()+"/"+request.getStartTime()+"/"+request.getPromotion()+"/"+request.getDistrict()
                + "&requestId=" + request.getOrderId() + "&requestType=captureWallet";


        String signatureHmac = "";
        try {
            signatureHmac = Utilities.calculateHMac(sign, Common.HMACSHA256, Common.SECRET_KEY);
            System.out.println("signature: " + signatureHmac + "   ");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Lỗi");
        }

        momoReq.setPartnerCode(Common.PARTNER_CODE);
        momoReq.setSignature(signatureHmac);
        momoReq.setAmount(Long.valueOf(amount));
        momoReq.setExtraData("");
        momoReq.setIpnUrl(Common.IPN_URL_MOMO);
        momoReq.setLang("vi");
        momoReq.setOrderId(request.getOrderId());
        momoReq.setOrderInfo("Thanh toan momo");
        momoReq.setRedirectUrl(Common.REDIRECT_URL_MOMO + request.getType() + "/" + request.getUserId() +"/" + request.getAddress() +"/"+request.getStartDate()+"/"+request.getEndDate()+"/"+request.getDescription()+"/"+request.getElderId()+"/"+request.getLatitude()+"/"+request.getLongitude()+"/"+request.getPackageId()+"/"+request.getDates()+"/"+request.getStartTime()+"/"+request.getPromotion()+"/"+request.getDistrict());
        momoReq.setRequestId(request.getOrderId());
        momoReq.setRequestType("captureWallet");

        HttpEntity<MomoRequest> req = new HttpEntity<>(momoReq, headers);
        try {
            ResponseEntity<MomoResponse> response = restTemplate.postForEntity(url, req, MomoResponse.class);

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            String arr[] = String.valueOf(e.getMessage()).split(",");
            String ar[] = arr[1].split(":");
            String message = ar[1].replaceAll("\"", "");
            System.out.println("" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Lỗi");
        }
        return null;
    }


//    @Override
//    public Boolean payDeposit(AddTransactionDTO addTransactionDTO) {
//        Booking booking = bookingRepository.findById(addTransactionDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy booking nào"));
//        CustomerProfile customerProfile = customerProfileRepository.findById(booking.getCustomer().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy khách hàng nào"));
//        Account admin = accountRepository.findByRole_Name("ADMIN");
//        if (addTransactionDTO.getPaymentMethod().equals(PaymentMethod.WALLET.toString())) {
//            Wallet customerWallet = walletRepository.findById(booking.getCustomer().getId()).get();
//            if (customerWallet.getAmount().compareTo(booking.getDeposit()) < 0) {
//                throw new ResponseStatusException(HttpStatus.valueOf(400), "Tài khoản trong ví của bạn khg đủ để thực hiện giao dịch vui lòng nạp thêm");
//            } else {
//                customerWallet.setAmount(customerWallet.getAmount().subtract(booking.getDeposit()));
//                walletRepository.save(customerWallet);
//                Wallet sitterWallet = walletRepository.findById(booking.getSitter().getId()).get();
//                sitterWallet.setAmount(sitterWallet.getAmount().add(booking.getTotalPrice().subtract(booking.getDeposit())));
//                walletRepository.save(sitterWallet);
//                Wallet adminWallet = walletRepository.findById(admin.getId()).get();
//                adminWallet.setAmount(adminWallet.getAmount().add(booking.getDeposit()));
//                walletRepository.save(adminWallet);
//            }
//        } else {
//            Wallet sitterWallet = walletRepository.findById(booking.getSitter().getId()).get();
//            sitterWallet.setAmount(sitterWallet.getAmount().add(booking.getTotalPrice().subtract(booking.getDeposit())));
//            walletRepository.save(sitterWallet);
//            Wallet adminWallet = walletRepository.findById(admin.getId()).get();
//            adminWallet.setAmount(adminWallet.getAmount().add(booking.getDeposit()));
//            walletRepository.save(adminWallet);
//        }
//        try {
//            Transaction transaction = Transaction.builder()
//                    .type(StatusCode.FULL_BOOKING.toString())
//                    .paymentMethod(addTransactionDTO.getPaymentMethod())
//                    .amount(booking.getTotalPrice())
//                    .createDate(LocalDate.now())
//                    .booking(booking)
//                    .customer(customerProfile)
//                    .build();
//            transactionRepository.save(transaction);
//            booking.setStatus(StatusCode.PAID.toString());
//            bookingRepository.save(booking);
//
//
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), "Thanh toán không thàng công xin vui lòng thử lại");
//        }
//        return true;
//    }


    @Override
    public ResponseEntity<MomoResponse> payBookingOnline(AddBookingV2DTO addBookingV2DTO) {
        CheckPriceRequestDTO checkPriceRequestDTO = CheckPriceRequestDTO.builder()
                .date(addBookingV2DTO.getDates())
                .packageId(addBookingV2DTO.getPackageId())
                .startTime(addBookingV2DTO.getStartTime())
                .promotion(addBookingV2DTO.getPromotion())
                .build();
        String date ="";
        List<LocalDate> dates = addBookingV2DTO.getDates();
        for (LocalDate localDate: dates) {
            date=date+localDate.toString()+" ";
        }

        BigDecimal total = bookingService.checkDateRangePrice(checkPriceRequestDTO).getTotalPrice();
        Long amount = total.longValue();
        String code = Utilities.randomAlphaNumeric(10);
        MomoClientRequest request = MomoClientRequest.builder()
                .bookingId("100")
                .address(addBookingV2DTO.getAddress())
                .startDate(addBookingV2DTO.getStartDate().toString())
                .endDate(addBookingV2DTO.getEndDate().toString())
                .description(addBookingV2DTO.getDescription())
                .elderId(addBookingV2DTO.getElderId())
                .latitude(addBookingV2DTO.getLatitude().toString())
                .longitude(addBookingV2DTO.getLongitude().toString())
                .packageId(addBookingV2DTO.getPackageId())
                .dates(date)
                .startTime(addBookingV2DTO.getStartTime().toString())
                .promotion(addBookingV2DTO.getPromotion())
                .district(addBookingV2DTO.getDistrict())
                .userId(addBookingV2DTO.getCustomerId())
                .type(TypeCode.DEPOSIT.toString())
                .orderId(code)
                .amount(amount)
                .build();
        ResponseEntity<MomoResponse> response = null;
        response = paymentService.getPaymentMomo(request);
        return  response;

    }
}
