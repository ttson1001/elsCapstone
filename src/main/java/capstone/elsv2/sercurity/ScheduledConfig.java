package capstone.elsv2.sercurity;

import capstone.elsv2.emunCode.BookingDetailStatus;
import capstone.elsv2.emunCode.PaymentMethod;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ScheduledConfig {
    @Autowired
    SitterProfileRepository sitterProfileRepository;

    @Autowired
    BookingDetailRepository bookingDetailRepository;
    @Autowired
    NotificationService notificationService;

    private final ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    @Scheduled(cron = "0 0 0 1 * ?", zone = "Asia/Ho_Chi_Minh")
    public void resetNumberOfCancel() {
        List<SitterProfile> sitterProfiles = sitterProfileRepository.findAllByAccount_Status(StatusCode.ACTIVATE.toString());
        for (SitterProfile sitterProfile : sitterProfiles) {
            if (sitterProfile.getNumberOfCancels() == null) sitterProfile.setNumberOfCancels(0);
            sitterProfile.setNumberOfCancels(0);
        }
    }

    //    @Scheduled(cron = "0 * * ? * *", zone = "Asia/Ho_Chi_Minh")
//    @Transactional
//    public void notificationForSitter() {
//        LocalDateTime localDateTime = LocalDateTime.now(istZoneId);
//        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByStartDateTime(localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), localDateTime.getYear());
//        for (BookingDetail bookingDetail : bookingDetails) {
//            Duration duration = Duration.between(LocalDateTime.now(istZoneId), bookingDetail.getStartDateTime());
//            SitterProfile sitter = bookingDetail.getBooking().getSitter();
//            long minutes = duration.toMinutes();
//            if (bookingDetail.getStatus().equals(BookingDetailStatus.WAITING.toString())) {
////                System.out.println(minutes);
////                System.out.println(bookingDetail.getStartDateTime());
////                System.out.println("SON" + LocalDateTime.now());
//                if (minutes == 10) {
////                    System.out.println("10 phút nữa đến giờ làm việc");
//                    notificationService.sendNotification(sitter.getId(),"Thông báo", "Còn 10 phút nữa dến giờ làm việc vui lòng check_in");
//                }else if (minutes == 0) {
////                    System.out.println("Đã đến giờ làm việc vui lòng thực hieện check in");
//                    notificationService.sendNotification(sitter.getId(),"Thông báo", "Đã dến giờ làm việc vui lòng check_in");
//                } else if (minutes == -20) {
////                    System.out.println("Vui lòng check in nếu không 10 phút nữa hệ thống sẽ xem như bạn vấng mặt ngày hôm nay");
//                    notificationService.sendNotification(sitter.getId(),"Thông báo", "Vui lòng check in nếu không 10 phút nữa hệ thống sẽ xem như bạn vấng mặt ngày hôm nay");
//                } else if (minutes <= -30) {
////                    System.out.println("Vì bạn khg check in đúng giờ nên hệ thống xem như bạn đã khg làm việc ngày hôm nay");
//                    notificationService.sendNotification(sitter.getId(),"Thông báo", "Vì bạn khg check in đúng giờ nên hệ thống xem như bạn đã khg làm việc ngày hôm nay");
//                    bookingDetail.setStatus(StatusCode.CANCEL.toString());
//                    bookingDetailRepository.save(bookingDetail);
//                    refundOneDay(bookingDetail);
//                }
//
//
//            }
//            if(bookingDetail.getStatus().equals(BookingDetailStatus.WORKING.toString())) {
//                if (minutes == 0) {
////                    System.out.println("Đã đến giờ làm việc vui lòng thực hieện check in");
//                    notificationService.sendNotification(sitter.getId(), "Thông báo", "Đã dến giờ check out");
//                } else if (minutes <= -30) {
//                    notificationService.sendNotification(sitter.getId(),"Thông báo", "Vì bạn khg check out đúng giờ nên hệ thống xem như bạn đã không làm việc ngày hôm nay");
//                    bookingDetail.setStatus(StatusCode.CANCEL.toString());
//                    bookingDetailRepository.save(bookingDetail);
//                    refundOneDay(bookingDetail);
//                }
//            }
//
//        }
//
//    }
    private void refundOneDay(BookingDetail bookingDetail) {
        Booking booking = bookingRepository.findById(bookingDetail.getBooking().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
        Account account = accountRepository.findByRole_Name("ADMIN");
        Wallet walletCus = walletRepository.findById(booking.getCustomer().getId()).get();
        walletCus.setAmount(walletCus.getAmount().add(bookingDetail.getPrice()));
        walletCus = walletRepository.save(walletCus);

        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletCus)
                .amount(bookingDetail.getPrice())
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(booking)
                .build();
        walletTransactionRepository.save(walletTransaction);

        Wallet walletAdmin = walletRepository.findById(account.getId()).get();
        walletAdmin.setAmount(walletAdmin.getAmount().subtract(bookingDetail.getPrice()));
        walletAdmin = walletRepository.save(walletAdmin);

        Transaction walletTransactionAD = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletAdmin)
                .amount(bookingDetail.getPrice())
                .booking(booking)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .build();
        walletTransactionRepository.save(walletTransactionAD);
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void notificationForSitter() {
        LocalDateTime localDateTime = LocalDateTime.now(istZoneId);
        List<Promotion> promotions = promotionRepository.findAllByStatus(StatusCode.ACTIVATE.toString());
        for (Promotion promotion : promotions) {
            if (promotion.getEndDate().isBefore(localDateTime.toLocalDate())) {
                promotion.setStatus(promotion.getStatus());
                promotionRepository.save(promotion);
            }
        }
    }


}
