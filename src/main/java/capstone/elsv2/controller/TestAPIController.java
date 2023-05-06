package capstone.elsv2.controller;

import capstone.elsv2.dto.common.Common;
import capstone.elsv2.dto.common.EmailDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;

import capstone.elsv2.dto.notification.NotificationResponseDTO;
import capstone.elsv2.dto.payment.AddWalletDTO;
import capstone.elsv2.dto.payment.MomoClientRequest;
import capstone.elsv2.dto.payment.MomoConfirmResultResponse;
import capstone.elsv2.dto.payment.MomoResponse;
//import capstone.elsv2.dto.testDTO.*;

import capstone.elsv2.emunCode.BookingDetailStatus;
import capstone.elsv2.emunCode.PaymentMethod;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.repositories.*;
import capstone.elsv2.sercurity.Utilities;
import capstone.elsv2.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("api-chi-de-test")
public class TestAPIController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    CusService cusService;

    @Autowired
    SitterService sitterService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerProfileRepository customerProfileRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    PaymentService paymentService;


    @GetMapping("test/{pageNumber}/{pageSize}")
    @PermitAll
    public ResponseEntity<ResponseDTO> test(@PathVariable int pageNumber, @PathVariable int pageSize) {
        ResponseDTO responseDTO = new ResponseDTO();
        pageNumber -= 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);
        List<Booking> bookings = bookingPage.getContent();
        System.out.println("Tổng số page: " + bookingPage.getTotalPages());
        System.out.println("Số page hiện tại: " + (bookingPage.getNumber() + 1));
        System.out.println("Trang tiep theo:" + bookingPage.hasNext());
        System.out.println("Trang trước đó:" + bookingPage.hasPrevious());
        System.out.println("So element trong page: " + bookingPage.getSize());
        System.out.println("Số record: " + bookingPage.getTotalElements());
        int offset = bookingPage.getNumber() * bookingPage.getSize() + 1;
        System.out.println("từ record :" + offset);
        responseDTO.setData("tới record :" + (offset + bookingPage.getNumberOfElements() - 1));
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("test/sitters")
//    @PermitAll
//    public ResponseEntity<ResponseDTO> test() {
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setData(sitterService.findAllSitter());
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @GetMapping("test1/{keyWord}/{pageNumber}/{pageSize}")
    @PermitAll
    public ResponseEntity<ResponseDTO> test1(@PathVariable String keyWord, @PathVariable int pageNumber, @PathVariable int pageSize) {
        ResponseDTO responseDTO = new ResponseDTO();
        keyWord = "";
        PageDTO bookings = cusService.searchByKeyWord(keyWord, pageNumber, pageSize);
        responseDTO.setData(bookings);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("test2")
    @PermitAll
    public ResponseEntity<ResponseDTO> test2() {
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(key,value);
        RestTemplate restTemplate = new RestTemplate();
        ResponseDTO responseDTO = new ResponseDTO();
        HttpEntity<ResponseDTO> request = new HttpEntity<>(responseDTO, httpHeaders);
        restTemplate.postForObject("https://provinces.open-api.vn/api/", request, ResponseDTO.class);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("test/api/choCuong")
    @PermitAll
    public ResponseEntity<ResponseDTO> testC() {
        BookingDetail bookingDetail = bookingDetailRepository.findById("77a7dcd9-d84f-4790-ad4a-dfcc2b8c95b8").get();
        bookingDetail.setStatus(BookingDetailStatus.WAITING.toString());
        bookingDetailRepository.save(bookingDetail);
        return ResponseEntity.ok().body(null);
    }

    @Autowired
    EmailService emailService;

    @GetMapping("test/nap-card")
    @PermitAll
    public ResponseEntity<ResponseDTO> tests(@RequestParam String email, @RequestParam BigDecimal amount){
        Account account = accountRepository.findByEmail(email);
        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.TOP_UP.toString())
                .createDateTime(LocalDateTime.now())
                .wallet(account.getWallet())
                .amount(amount)
                .paymentMethod(PaymentMethod.MOMO.toString())
                .build();
        walletTransactionRepository.save(walletTransaction);
        Wallet wallet = walletRepository.findById(account.getId()).get();
        wallet.setAmount(wallet.getAmount().add(amount));
        walletRepository.save(wallet);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("test4")
    @PermitAll
    public ResponseEntity<ResponseDTO> test4(@RequestBody EmailDTO emailDTO) {

        emailService.sendSimpleMail(emailDTO);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("test5/{keyWord}")
    @PermitAll
    public ResponseEntity<ResponseDTO> test5(@PathVariable String keyWord) {
        ResponseDTO responseDTO = new ResponseDTO();
        SitterProfile sitterProfile = sitterProfileRepository.findById(keyWord).get();
        String mon = "MONDAY";
        String tue = "TUESDAY";
        String wed = "WEDNESDAY";
        String thu = "THURSDAY";
        String fri = "FRIDAY";
        String sat = "SATURDAY";
        String sun = "SUNDAY";

        List<String> dayOfWeek = new ArrayList<>();
        dayOfWeek.add(mon);
        dayOfWeek.add(tue);
        dayOfWeek.add(wed);
        dayOfWeek.add(thu);
        dayOfWeek.add(fri);
        dayOfWeek.add(sat);
        dayOfWeek.add(sun);

        for (String day : dayOfWeek) {
            WorkingTime workingTime = WorkingTime.builder()
                    .sitter(sitterProfile)
                    .dayOfWeek(day)
                    .status(StatusCode.DEACTIVATE.toString())
                    .build();
            workingTimeRepository.save(workingTime);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @Autowired
    AdminService adminService;
    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    @Autowired
    private SitterProfileRepository sitterProfileRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @GetMapping("test3/{localDate}")
    @PermitAll
    public ResponseEntity<ResponseDTO> test3(@PathVariable String localDate) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(adminService.getRevenueForDate(LocalDate.parse(localDate)));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_link")
    @PermitAll
    public  ResponseEntity test(){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://elscusv2.page.link/success"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("add_common_for_sql")
    @PermitAll
    public ResponseEntity<ResponseDTO> test1() {
        ResponseDTO responseDTO = new ResponseDTO();
        Role role = Role.builder()
                .name("ADMIN")
                .build();
        Role role1 = Role.builder()
                .name("CUSTOMER")
                .build();
        Role role2 = Role.builder()
                .name("SITTER")
                .build();
        Role role3 = Role.builder()
                .name("STAFF")
                .build();
        roleRepository.save(role);
        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        Account account = Account.builder()
                .email("admin")
                .role(role)
                .phone("0000000001")
                .fullName("Hồ Thị Kỉ")
                .password(passwordEncoder.encode("Els12345"))
                .status("ACTIVE")
                .build();
        Account account3 = Account.builder()
                .email("staff")
                .fullName("HỒ Minh Hiếu")
                .phone("0000000000")
                .password(passwordEncoder.encode("Els12345"))
                .status("ACTIVE")
                .role(role3)
                .build();
        account = accountRepository.save(account);
        Wallet wallet = Wallet.builder()
                .account(account)
                .amount(BigDecimal.valueOf(0))
                .build();
        walletRepository.save(wallet);
        account3 = accountRepository.save(account3);
        Wallet wallet1 = Wallet.builder()
                .account(account3)
                .amount(BigDecimal.valueOf(0))
                .build();
        walletRepository.save(wallet1);
        return ResponseEntity.ok().body(responseDTO);
    }


}
