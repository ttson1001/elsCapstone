package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.payment.*;
import capstone.elsv2.emunCode.PaymentMethod;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.Wallet;
import capstone.elsv2.entities.Transaction;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.BookingRepository;
import capstone.elsv2.repositories.WalletRepository;
import capstone.elsv2.repositories.WalletTransactionRepository;
import capstone.elsv2.sercurity.Utilities;
import capstone.elsv2.services.NotificationService;
import capstone.elsv2.services.PaymentService;
import capstone.elsv2.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    PaymentService paymentService;
    private final BookingRepository bookingRepository;
    private final AccountRepository accountRepository;
    private final WalletTransactionRepository walletTransaction;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             BookingRepository bookingRepository,
                             AccountRepository accountRepository,
                             WalletTransactionRepository walletTransaction,
                             WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
        this.walletTransaction = walletTransaction;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    @Transactional
    public Boolean addWallet(AddWalletDTO addWalletDTO) {
        if(addWalletDTO.getAmount().compareTo(BigDecimal.valueOf(5000)) <0 ||
                addWalletDTO.getAmount().compareTo(BigDecimal.valueOf(5000000)) >0) throw new ResponseStatusException(HttpStatus.valueOf(400), "Chỉ cho add số tiền trong khoảng từ 5.000VND tới 50.000.000VND");
        Wallet wallet = walletRepository.findById(addWalletDTO.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy wallet của người dùng"));
        wallet.setAmount(addWalletDTO.getAmount().add(wallet.getAmount()));
        walletRepository.save(wallet);
        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.TOP_UP.toString())
                .createDateTime(LocalDateTime.now())
                .wallet(wallet)
                .amount(addWalletDTO.getAmount())
                .paymentMethod(PaymentMethod.MOMO.toString())
                .build();
        walletTransactionRepository.save(walletTransaction);
        notificationService.sendNotification(addWalletDTO.getCustomerId(), "Thông báo", "Nạp thành công " + addWalletDTO.getAmount() + " VND");
        return true;
    }

    @Override
    public ResponseEntity<MomoResponse> addWalletV2(AddWalletDTO addWalletDTO) {
        Long amount = Long.parseLong(addWalletDTO.getAmount().toString());
        String code = Utilities.randomAlphaNumeric(10);
        MomoClientRequest request = MomoClientRequest.builder()
                .bookingId("100")
                .userId(addWalletDTO.getCustomerId())
                .type(TypeCode.TOP_UP.toString())
                .orderId(code)
                .amount(amount)
                .build();
        ResponseEntity<MomoResponse> response = null;
        response = paymentService.getPaymentMomo(request);
        return response;
    }

    @Override
    public WalletDTO getWallet(String userId) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy wallet của người dùng"));
        WalletDTO walletDTO = WalletDTO.builder()
                .amount(wallet.getAmount())
                .build();
        return walletDTO;
    }

    @Override
    public WalletDTO getWalletSystem() {
        Account accountAdmin = accountRepository.findByRole_Name("ADMIN");
        Wallet wallet = walletRepository.findById(accountAdmin.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy wallet của người dùng"));
        WalletDTO walletDTO = WalletDTO.builder()
                .amount(wallet.getAmount())
                .build();
        return walletDTO;
    }

    @Override
    @Transactional
    public Boolean refund(RefundDTO refundDTO) {
        Booking booking = bookingRepository.findById(refundDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy booking nào"));
        Wallet wallet = walletRepository.findById(booking.getCustomer().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy wallet của người dùng"));
        Account account = accountRepository.findByRole_Name("ADMIN");
        if (refundDTO.getPercentRefund() > 0) {
            BigDecimal refund = booking.getDeposit().multiply(BigDecimal.valueOf(refundDTO.getPercentRefund()));
            wallet.setAmount(wallet.getAmount().add(refund));
            walletRepository.save(wallet);
            Transaction walletTransaction = Transaction.builder()
                    .type(TypeCode.REFUND.toString())
                    .createDateTime(LocalDateTime.now())
                    .wallet(wallet)
                    .amount(refund)
                    .paymentMethod(PaymentMethod.WALLET.toString())
                    .build();
            walletTransactionRepository.save(walletTransaction);
            Wallet adminWallet = walletRepository.findById(account.getId()).get();
            adminWallet.setAmount(adminWallet.getAmount().subtract(refund));
            walletRepository.save(adminWallet);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Bạn đã được hoàn lại " + refund + " VND, Sau khi chúng tôi đã xem xét");
        } else {
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Bạn Không được hoàn lại tiền vì " + refundDTO.getResponse());
        }
        return true;
    }

    @Override
    public List<TransactionWalletDTO> getAllTransaction(String userId) {
        List<TransactionWalletDTO> transactionWalletDTOS = new ArrayList<>();
        List<Transaction> walletTransactions = walletTransactionRepository.findAllByWallet_IdOrderByCreateDateTimeDesc(userId);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        for (Transaction walletTransaction : walletTransactions) {
            if (walletTransaction.getType().equals(TypeCode.TOP_UP.toString()) || walletTransaction.getType().equals(TypeCode.REFUND.toString()) || walletTransaction.getType().equals(TypeCode.RECEIVE.toString())) {
                TransactionWalletDTO transactionWalletDTO = TransactionWalletDTO.builder()
                        .id(walletTransaction.getId())
                        .createDateTime(walletTransaction.getCreateDateTime())
                        .amount("+ " + currencyVN.format(walletTransaction.getAmount()))
                        .type(walletTransaction.getType())
                        .build();
                transactionWalletDTOS.add(transactionWalletDTO);
            } else {
                TransactionWalletDTO transactionWalletDTO = TransactionWalletDTO.builder()
                        .id(walletTransaction.getId())
                        .createDateTime(walletTransaction.getCreateDateTime())
                        .amount("- " + currencyVN.format(walletTransaction.getAmount()))
                        .type(walletTransaction.getType())
                        .build();
                transactionWalletDTOS.add(transactionWalletDTO);
            }
        }
        return transactionWalletDTOS;
    }
//
}
