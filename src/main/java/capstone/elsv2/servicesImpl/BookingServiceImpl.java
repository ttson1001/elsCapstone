package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.booking.*;
import capstone.elsv2.dto.booking.request.AddDateBookingDTO;
import capstone.elsv2.dto.booking.request.CheckPriceRequestDTO;
import capstone.elsv2.dto.booking.response.BookingResponseDTO;
import capstone.elsv2.dto.booking.response.CheckPriceResponseDTO;
import capstone.elsv2.dto.common.DateRangeDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.SettingDTO;
import capstone.elsv2.dto.common.SlotDTOV2;
import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.elder.ElderDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.rating.RatingHistoryDTO;
import capstone.elsv2.dto.report.ReportHistoryDTO;
import capstone.elsv2.dto.sitter.SitterDTO;
import capstone.elsv2.dto.workingTime.WorkingTimeDTO;
import capstone.elsv2.dto.workingTime.WorkingTimeDTOV2;
import capstone.elsv2.emunCode.*;
import capstone.elsv2.entities.*;
import capstone.elsv2.entities.Package;
import capstone.elsv2.mapper.BookingMapper;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.*;
import capstone.elsv2.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SitterPackageRepository sitterPackageRepository;
    @Autowired
    private ElderRepository elderRepository;

    @Autowired
    private CommonService commonService;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private SitterProfileRepository sitterProfileRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private HolidayDateRepository holidayDateRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private WorkingTimeService workingTimeService;
    @Autowired
    private AssigningService assigningService;
    @Autowired
    private PromotionRepository promotionRepository;

    private final String ERROR_DEFAULT_ADDRESS = "Vui lòng cập nhật địa chỉ mặt đinh trước khi đặt lịch";
    private final String ERROR_END_DATE_BEFORE_START_DATE = "Không thể đặt ngày bắt đầu trước ngày kết thúc";
    private final String ERROR_BOOKING = "Không thể đặt lịch được";
    private final String ERROR_WALLET = "Không đủ tiền trong ví để thực hiện việc đặt lịch vui lòng nạp thêm ";
    private final String ERROR_NOT_FOUND = "Không tìm đơn hàng nào";
    public static final String ERROR_NOT_FOUND_CUSTOMER = "Khách hàng không tồn tại";
    public static final String ERROR_DEACTIVATE_CUSTOMER = "Khách hàng không có quyền hạn";
    public static final String ERROR_NOT_FOUND_ELDER = "Người cao tuổi không tồn tại";
    public static final String ERROR_NOT_FOUND_PACKAGE = "Gói dịch vụ không tồn tại";
    public static final String ERROR_NOT_FOUND_BOOKING = "Booking không tồn tại";
    public static final String ERROR_NOT_FOUND_SETTING = "Booking không tồn tại";
    public static final String ERROR_NOT_FOUND_WALLET = "Ví tiền không tồn tại";
    public static final String ERROR_OVERLAP_DATE_TIME_BOOKING = "Người thân này đã có lịch đặt trong ngày ";
    public static final String ERROR_BOOKING_DATE_START_MUST_BEFORE_CURRENT_TWO_DAY = "Thời gian booking phải sớm hơn thời gian bắt đầu 2 ngày";
    public static final String ERROR_NOT_FOUND_SITTER = "Không tìm thấy sitter. Hãy tìm lại sau";
    public static final String ERROR_STATUS_BOOKING = "Trạng thái booking không thể thực hiện hành động này";
    public static final String ERROR_CHANGE_BOOKING = "Chỉ được đổi 1 lần trong mỗi booking";

    public static final String ERROR_DATE_TIME_BOOKING_CHANGE_SITTER = "Thời gian thay đổi sitter phải sớm hơn thời gian ngày đặt lịch kế tiếp 12 tiếng";
    public static final String ERROR_NOT_FOUND_PROMOTION = "Không tìm thấy mã giảm giá. Hãy tìm lại sau";
    public static final String ERROR_INVALID_PROMOTION = "Mã giảm giá đã hết hạn. Hãy thử mã khác";
    private Map<Integer, SlotDTOV2> slotSystem;

    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private TrackingRepository trackingRepository;
    private final ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    @Autowired
    private PackageServiceRepository packageServiceRepository;
    private Setting setting;

    @PostConstruct
    public void init() {
        setting = settingRepository.findById("1")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_SETTING));
        slotSystem = commonService.getAllSlotMap();
    }

    @Override
    @Transactional()
    public BookingResponseDTO addBookingV2(AddBookingV2DTO addBookingDTO) {
        Booking booking = null;

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        Account customer = accountRepository.findById(addBookingDTO.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_CUSTOMER));

        if (customer.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DEACTIVATE_CUSTOMER);
        }

        Elder elder = elderRepository.findById(addBookingDTO.getElderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_ELDER));

        Package packageEntity = packageRepository.findById(addBookingDTO.getPackageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PACKAGE));

        if (addBookingDTO.getAddress().equals("") && customer.getCustomerProfile().getAddress() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DEFAULT_ADDRESS);
        }

        if (addBookingDTO.getEndDate().isBefore(addBookingDTO.getStartDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_END_DATE_BEFORE_START_DATE);


        //validate booking start before 2 day current
//        if (LocalDate.now(istZoneId).plusDays(2).isAfter(addBookingDTO.getStartDate())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_BOOKING_DATE_START_MUST_BEFORE_CURRENT_TWO_DAY);
//        }

        //check duplicate booking package
        //get all booking detail of elder from date to date to check time duplicate
        List<BookingDetail> bookingDetailList = bookingDetailRepository.getListBookingDetailToValidDateTime(addBookingDTO.getElderId(), addBookingDTO.getStartDate());

        List<AddBookingDetailDTO> addBookingDetailDTOList = new ArrayList<>();

        //TODO: checkListDate and in dateStart-> dateEnd range

        for (LocalDate date : addBookingDTO.getDates()) {
            LocalDateTime dateTimeStart = LocalDateTime.of(date, addBookingDTO.getStartTime());
            LocalDateTime dateTimeEnd = dateTimeStart.plusHours(packageEntity.getDuration());
            addBookingDetailDTOList.add(new AddBookingDetailDTO(dateTimeStart, dateTimeEnd));
            while (dateTimeStart.isBefore(dateTimeEnd)) {
                bookingDetailList.add(new BookingDetail(dateTimeStart, dateTimeStart.plusHours(2)));
                dateTimeStart = dateTimeStart.plusHours(2);
            }
        }

        //check time duplicate
        if (bookingDetailList.size() > 1) {
            LocalDate overlapDate = getOverLapDateBookingDetailList(bookingDetailList);
            if (!ValidateUtil.isNullOrEmpty(overlapDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_OVERLAP_DATE_TIME_BOOKING + overlapDate);
            }
        }

        String slotBooking = "";
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        List<BookingDetail> listBookingDetail = new ArrayList<>();
        List<DetailService> detailServiceList = new ArrayList<>();

        for (PackageService packageService : packageEntity.getPackageServices()) {
            capstone.elsv2.entities.Service service = serviceRepository.findById(packageService.getService().getId()).get();
            DetailService detailService = DetailService.builder()
                    .serviceDuration(service.getDuration())
                    .servicePrice(service.getPrice())
                    .serviceName(service.getName())
                    .build();
            detailServiceList.add(detailService);
        }


        booking = Booking.builder()
                .address(addBookingDTO.getAddress())
                .createDate(LocalDate.now(istZoneId))
                .latitude(addBookingDTO.getLatitude())
                .Longitude(addBookingDTO.getLongitude())
                .startDate(addBookingDTO.getStartDate())
                .endDate(addBookingDTO.getEndDate())
                .description(addBookingDTO.getDescription())
                .status(BookingStatus.PENDING.toString())
                .elder(elder)
                .customer(customer.getCustomerProfile())
                .build();
        LocalDateTime dateStart = null;
        LocalDateTime dateEnd = null;
        Set<LocalDate> holidayDates = getListDate();
        for (AddBookingDetailDTO addBookingDetailDTO : addBookingDetailDTOList) {
            dateStart = addBookingDetailDTO.getStartDateTime();
            dateEnd = addBookingDetailDTO.getEndDateTime();

            //caculate price
            //TODO: dateEnd base slot end
            BigDecimal percentChange = caculatePercent(dateStart, dateEnd, holidayDates);
            BigDecimal priceEachBookingDetail = packageEntity.getPrice().multiply(percentChange);

            BookingDetail bookingDetail = BookingDetail.builder()
                    .booking(booking)
                    .startDateTime(dateStart)
                    .endDateTime(dateEnd)
                    .estimateTime(packageEntity.getDuration())
                    .packageName(packageEntity.getName())
                    .price(priceEachBookingDetail)
                    ._package(packageEntity)
                    .status(BookingDetailStatus.WAITING.toString())
                    .percentChange(percentChange)
                    .build();

            totalPrice = totalPrice.add(priceEachBookingDetail);
            slotBooking = slotBooking.concat(getDateSlotBookingDetail(dateStart, packageEntity.getDuration()));

            detailServiceList.forEach(detailService -> detailService.setBookingDetail(bookingDetail));
            List<DetailService> addList = new ArrayList<>();
            for (DetailService detailService : detailServiceList) {
                DetailService d = DetailService.builder()
                        .serviceDuration(detailService.getServiceDuration())
                        .servicePrice(detailService.getServicePrice())
                        .serviceName(detailService.getServiceName())
                        .bookingDetail(bookingDetail)
                        .build();
                addList.add(d);
            }
            bookingDetail.setDetailServices(addList);
            listBookingDetail.add(bookingDetail);
        }
        //set promo
        if (!ValidateUtil.isNullOrEmpty(addBookingDTO.getPromotion())) {
            Promotion promotion = promotionRepository.findById(addBookingDTO.getPromotion())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PROMOTION));
            LocalDate dateNow = LocalDate.now(istZoneId);
            if (dateNow.isAfter(promotion.getEndDate()) || dateNow.isBefore(promotion.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_INVALID_PROMOTION);
            }
            BigDecimal discountPrice = totalPrice.multiply(BigDecimal.valueOf(promotion.getValue()));
            discountPrice = discountPrice.setScale(0, BigDecimal.ROUND_HALF_EVEN);
            booking.setPromotion(promotion);
            totalPrice = totalPrice.subtract(discountPrice);
        }
        booking.setStartTime(dateStart.toLocalTime());
        booking.setEndTime(dateEnd.toLocalTime());
        booking.setBookingDetails(listBookingDetail);
        booking.setSlots(slotBooking);
        booking.setTotalPrice(totalPrice);
        booking.setDeposit(totalPrice);
        booking.setDistrict(addBookingDTO.getDistrict());

        List<SitterProfile> listSitterPriority = getAvailableSitter(booking, addBookingDTO.getDistrict(), null);
        if (ValidateUtil.isNullOrEmpty(listSitterPriority)) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_NOT_FOUND_SITTER);
        }

        Wallet customerWallet = walletRepository.findById(customer.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_WALLET)
        );
        if (customerWallet.getAmount().compareTo(totalPrice) < 0) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_WALLET + currencyVN.format(totalPrice.subtract(customerWallet.getAmount())));
        }

        booking = bookingRepository.save(booking);

        customerWallet.setAmount(customerWallet.getAmount().subtract(totalPrice));
        walletRepository.save(customerWallet);
        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.DEPOSIT.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(customerWallet)
                .amount(totalPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(booking)
                .build();
        walletTransactionRepository.save(walletTransaction);
        Account admin = accountRepository.findByRole_Name("ADMIN");
        Wallet adminWallet = walletRepository.findById(admin.getId()).get();
        adminWallet.setAmount(adminWallet.getAmount().add(totalPrice));
        walletRepository.save(adminWallet);

        Transaction walletTransactionAdmin = Transaction.builder()
                .type(TypeCode.DEPOSIT.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(adminWallet)
                .amount(totalPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(booking)
                .build();
        walletTransactionRepository.save(walletTransactionAdmin);

        return BookingMapper.INSTANCE.bookingConvertToDTO(booking);
    }


    @Override
    public PageDTO getAllFormBooking(int pageNumber, int pageSize) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();//
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAllByStatus(pageable, BookingStatus.PENDING.toString());
        List<Booking> bookings = bookingPage.getContent();
        for (Booking booking : bookings) {
            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .totalPrice(booking.getTotalPrice())
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        int offset = bookingPage.getNumber() * bookingPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(bookingDTOS)
                .pageSize(bookingPage.getSize())
                .hasNextPage(bookingPage.hasNext())
                .pageIndex(bookingPage.getNumber() + 1)
                .totalRecord(bookingPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + bookingPage.getNumberOfElements() - 1)
                .hasPreviousPage(bookingPage.hasPrevious())
                .totalPages(bookingPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public PageDTO getAllBooking(int pageNumber, int pageSize) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);
        List<Booking> bookings = bookingPage.getContent();
        for (Booking booking : bookings) {
            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .totalPrice(booking.getTotalPrice())
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        bookingDTOS.sort(Comparator.comparing(p -> p.getCreateDate()));
        int offset = bookingPage.getNumber() * bookingPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(bookingDTOS)
                .pageSize(bookingPage.getSize())
                .hasNextPage(bookingPage.hasNext())
                .pageIndex(bookingPage.getNumber() + 1)
                .totalRecord(bookingPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + bookingPage.getNumberOfElements() - 1)
                .hasPreviousPage(bookingPage.hasPrevious())
                .totalPages(bookingPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public PageDTO getAllByStatusForAdmin(String status, int pageNumber, int pageSize) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Booking> bookingPage;
        if (status == null) {
            bookingPage = bookingRepository.findAll(pageable);
        } else {
            bookingPage = bookingRepository.findAllByStatus(pageable, status);
        }
        List<Booking> bookings = bookingPage.getContent();
        for (Booking booking : bookings) {
            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .totalPrice(booking.getTotalPrice())
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        int offset = bookingPage.getNumber() * bookingPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(bookingDTOS)
                .pageSize(bookingPage.getSize())
                .hasNextPage(bookingPage.hasNext())
                .pageIndex(bookingPage.getNumber() + 1)
                .totalRecord(bookingPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + bookingPage.getNumberOfElements() - 1)
                .hasPreviousPage(bookingPage.hasPrevious())
                .totalPages(bookingPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public List<BookingDTO> getAllByStatus(String status) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        if (status.equals("") || status == null) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findAllByStatus(status);
        }
        if (bookings.isEmpty())
            throw new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND);
        for (Booking booking : bookings) {
            ElderDTO elderDTO = ElderDTO.builder()
                    .id(booking.getElder().getId())
                    .fullName(booking.getElder().getFullName())
                    .gender(booking.getElder().getGender())
                    .dob(booking.getElder().getDob())
                    .healStatus(booking.getElder().getHealthStatus())
                    .build();
            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .elderDTO(elderDTO)
                    .totalPrice(booking.getTotalPrice())
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        bookingDTOS.sort(Comparator.comparing(b -> b.getCreateDate()));
        Collections.reverse(bookingDTOS);
        return bookingDTOS;
    }

    @Override
    public PageDTO getAllBookingHistory(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        Page<Booking> bookingPage = bookingRepository.findAllByEndDateBefore(pageable, LocalDate.now(istZoneId).plusDays(1));
        List<Booking> bookings = bookingPage.getContent();
        for (Booking booking : bookings) {
            LocalDateTime endDateTime = LocalDateTime.parse(booking.getEndDate() + "T" + booking.getEndTime());
            if (endDateTime.isBefore(LocalDateTime.now(istZoneId))) {
                if (booking.getStatus().equals(BookingStatus.CANCEL.toString()) || booking.getStatus().equals(BookingStatus.PAID.toString())) {
                    ElderDTO elderDTO = ElderDTO.builder()
                            .id(booking.getElder().getId())
                            .fullName(booking.getElder().getFullName())
                            .age(LocalDate.now(istZoneId).getYear() - booking.getElder().getDob().getYear())
                            .gender(booking.getElder().getGender())
                            .dob(booking.getElder().getDob())
                            .healStatus(booking.getElder().getHealthStatus())
                            .build();
                    BookingDTO bookingDTO = BookingDTO.builder()
                            .id(booking.getId())
                            .address(booking.getAddress())
                            .createDate(booking.getCreateDate())
                            .status(booking.getStatus())
                            .elderDTO(elderDTO)
                            .totalPrice(booking.getTotalPrice())
                            .build();
                    bookingDTOS.add(bookingDTO);
                }
            }
        }
        int offset = bookingPage.getNumber() * bookingPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(bookingDTOS)
                .pageSize(bookingPage.getSize())
                .hasNextPage(bookingPage.hasNext())
                .pageIndex(bookingPage.getNumber() + 1)
                .totalRecord(bookingPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + bookingPage.getNumberOfElements() - 1)
                .hasPreviousPage(bookingPage.hasPrevious())
                .totalPages(bookingPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public List<BookingDTO> getAllBookingHistoryByCustomer(String customerId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByEndDateBeforeAndCustomer_Id(LocalDate.now(istZoneId).plusDays(1), customerId);
        for (Booking booking : bookings) {
            LocalDateTime endDateTime = LocalDateTime.parse(booking.getEndDate() + "T" + booking.getEndTime());
            if (endDateTime.isBefore(LocalDateTime.now(istZoneId))) {
                if (booking.getStatus().equals(BookingStatus.COMPLETED.toString())
                        || booking.getStatus().equals(BookingStatus.PAID.toString())
                        || booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
                    SitterDTO sitterDTO = SitterDTO.builder()
                            .id(booking.getSitter().getId())
                            .fullName(booking.getSitter().getAccount().getFullName())
                            .phone(booking.getSitter().getAccount().getPhone())
                            .email(booking.getSitter().getAccount().getEmail())
                            .gender(booking.getSitter().getGender())
                            .status(booking.getSitter().getAccount().getStatus())
                            .build();
                    ElderDTO elderDTO = ElderDTO.builder()
                            .id(booking.getElder().getId())
                            .fullName(booking.getElder().getFullName())
                            .age(LocalDate.now(istZoneId).getYear() - booking.getElder().getDob().getYear())
                            .gender(booking.getElder().getGender())
                            .dob(booking.getElder().getDob())
                            .build();
                    BookingDTO bookingDTO = BookingDTO.builder()
                            .id(booking.getId())
                            .address(booking.getAddress())
                            .createDate(booking.getCreateDate())
                            .status(booking.getStatus())
                            .elderDTO(elderDTO)
                            .endDate(booking.getEndDate())
                            .startDate(booking.getStartDate())
                            .sitterDTO(sitterDTO)
                            .totalPrice(booking.getTotalPrice())
                            .build();
                    bookingDTOS.add(bookingDTO);
                }
            }
        }
        return bookingDTOS;
    }

    @Override
    public List<BookingDTO> getAllBookingHistoryBySitter(String sitterId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByEndDateBeforeAndSitter_Id(LocalDate.now(istZoneId).plusDays(1), sitterId);
        for (Booking booking : bookings) {
            LocalDateTime endDateTime = LocalDateTime.parse(booking.getEndDate() + "T" + booking.getEndTime());
            if (endDateTime.isBefore(LocalDateTime.now(istZoneId))) {
                if (booking.getStatus().equals(BookingStatus.COMPLETED.toString())
                        || booking.getStatus().equals(BookingStatus.PAID.toString())
                        || booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
                    ElderDTO elderDTO = ElderDTO.builder()
                            .id(booking.getElder().getId())
                            .fullName(booking.getElder().getFullName())
                            .age(LocalDate.now(istZoneId).getYear() - booking.getElder().getDob().getYear())
                            .gender(booking.getElder().getGender())
                            .dob(booking.getElder().getDob())
                            .build();
                    CustomerDTO customerDTO = CustomerDTO.builder()
                            .id(booking.getCustomer().getId())
                            .fullName(booking.getCustomer().getAccount().getFullName())
                            .phone(booking.getCustomer().getAccount().getPhone())
                            .email(booking.getCustomer().getAccount().getEmail())
                            .address(booking.getCustomer().getAddress())
                            .gender(booking.getCustomer().getGender())
                            .status(booking.getCustomer().getAccount().getStatus())
                            .build();
                    BookingDTO bookingDTO = BookingDTO.builder()
                            .id(booking.getId())
                            .address(booking.getAddress())
                            .createDate(booking.getCreateDate())
                            .status(booking.getStatus())
                            .endDate(booking.getEndDate())
                            .startDate(booking.getStartDate())
                            .customerDTO(customerDTO)
                            .elderDTO(elderDTO)
                            .totalPrice(booking.getTotalPrice())
                            .build();
                    bookingDTOS.add(bookingDTO);
                }
            }
        }
        return bookingDTOS;
    }

    @Override
    public List<BookingDTO> getAllByStatusAndCustomerId(String status, String customerId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByStatusAndCustomer_Id(status, customerId);
        if (bookings.isEmpty())
            return bookingDTOS;
        for (Booking booking : bookings) {
            SitterDTO sitterDTO = null;
            if (booking.getSitter() != null) {
                sitterDTO = SitterDTO.builder()
                        .id(booking.getSitter().getId())
                        .fullName(booking.getSitter().getAccount().getFullName())
                        .phone(booking.getSitter().getAccount().getPhone())
                        .email(booking.getSitter().getAccount().getEmail())
                        .gender(booking.getSitter().getGender())
                        .status(booking.getSitter().getAccount().getStatus())
                        .build();
            }
            CustomerDTO customerDTO = CustomerDTO.builder()
                    .id(booking.getCustomer().getId())
                    .fullName(booking.getCustomer().getAccount().getFullName())
                    .phone(booking.getCustomer().getAccount().getPhone())
                    .email(booking.getCustomer().getAccount().getEmail())
                    .address(booking.getCustomer().getAddress())
                    .gender(booking.getCustomer().getGender())
                    .status(booking.getCustomer().getAccount().getStatus())
                    .build();
            ElderDTO elderDTO = ElderDTO.builder()
                    .id(booking.getElder().getId())
                    .fullName(booking.getElder().getFullName())
                    .gender(booking.getElder().getGender())
                    .healStatus(booking.getElder().getHealthStatus())
                    .dob(booking.getElder().getDob())
                    .build();
            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .elderDTO(elderDTO)
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .sitterDTO(sitterDTO)
                    .customerDTO(customerDTO)
                    .totalPrice(booking.getTotalPrice())
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    @Override
    public BookingFullHistoryDTO getFullBookingHistory(String bookingId) {
        BookingFullHistoryDTO bookingFullHistoryDTO = null;
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        List<BookingDetail> bookingDetails = booking.getBookingDetails();
        List<BookingDetailFormDTO> bookingDetailFormDTOS = new ArrayList<>();
        List<ReportHistoryDTO> reportHistoryDTOS = new ArrayList<>();
        FeedBack rating = booking.getRating();
        SitterDTO sitterDTO = null;
        if (booking.getSitter() != null) {
            sitterDTO = SitterDTO.builder()
                    .id(booking.getSitter().getId())
                    .fullName(booking.getSitter().getAccount().getFullName())
                    .phone(booking.getSitter().getAccount().getPhone())
                    .email(booking.getSitter().getAccount().getEmail())
                    .gender(booking.getSitter().getGender())
                    .status(booking.getSitter().getAccount().getStatus())
                    .build();
        }

        ElderDTO elderDTO = ElderDTO.builder()
                .id(booking.getElder().getId())
                .fullName(booking.getElder().getFullName())
                .gender(booking.getElder().getGender())
                .healStatus(booking.getElder().getStatus())
                .dob(booking.getElder().getDob())
                .build();

        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(booking.getCustomer().getId())
                .fullName(booking.getCustomer().getAccount().getFullName())
                .phone(booking.getCustomer().getAccount().getPhone())
                .email(booking.getCustomer().getAccount().getEmail())
                .address(booking.getCustomer().getAddress())
                .gender(booking.getCustomer().getGender())
                .status(booking.getCustomer().getAccount().getStatus())
                .build();

        for (BookingDetail bookingDetail : bookingDetails) {
            List<DetailServiceDTO> detailServiceDTOS = new ArrayList<>();

            List<DetailService> detailServices = bookingDetail.getDetailServices();
            for (DetailService detailService : detailServices) {
                DetailServiceDTO detailServiceDTO = DetailServiceDTO.builder()
                        .id(detailService.getId())
                        .serviceName(detailService.getServiceName())
                        .servicePrice(detailService.getServicePrice())
                        .serviceDuration(detailService.getServiceDuration())
                        .build();
                detailServiceDTOS.add(detailServiceDTO);
            }
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(bookingDetail.get_package().getId())
                    .name(bookingDetail.get_package().getName())
                    .price(bookingDetail.get_package().getPrice())
                    .duration(bookingDetail.get_package().getDuration())
                    .desc(bookingDetail.get_package().getDescription())
                    .build();
            List<Report> reports = reportRepository.findAllByBookingDetail_Id(bookingDetail.getId());

            for (Report report : reports) {
                ReportHistoryDTO reportHistoryDTO = ReportHistoryDTO.builder()
                        .id(report.getId())
                        .content(report.getContent())
                        .title(report.getId())
                        .reply(report.getReply())
                        .createDate(report.getCreateDate())
                        .customerName(customerDTO.getFullName())
                        .sitterName(sitterDTO != null ? sitterDTO.getFullName() : null)
                        .status(report.getStatus())
                        .build();
                reportHistoryDTOS.add(reportHistoryDTO);
            }


            BookingDetailFormDTO bookingDetailFormDTO = BookingDetailFormDTO.builder()
                    .id(bookingDetail.getId())
                    .estimateTime(bookingDetail.getEstimateTime())
                    .packageName(packageDTO.getName())
                    .startDateTime(bookingDetail.getStartDateTime())
                    .endDateTime(bookingDetail.getEndDateTime())
                    .packageDTO(packageDTO)
                    .reportHistoryDTOS(reportHistoryDTOS)
                    .status(bookingDetail.getStatus())
                    .detailServiceDTOS(detailServiceDTOS)
                    .build();
            bookingDetailFormDTOS.add(bookingDetailFormDTO);
        }
        RatingHistoryDTO ratingHistoryDTO = null;
        if (rating != null) {
            ratingHistoryDTO = RatingHistoryDTO.builder()
                    .id(rating.getId())
                    .rate(rating.getRate())
                    .comment(rating.getComment())
                    .build();
        }

        bookingFullHistoryDTO = BookingFullHistoryDTO.builder()
                .id(booking.getId())
                .address(booking.getAddress())
                .createDate(booking.getCreateDate())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .deposit(booking.getDeposit())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .customerDTO(customerDTO)
                .elderDTO(elderDTO)
                .ratingHistoryDTO(ratingHistoryDTO)
                .sitterDTO(sitterDTO)
                .bookingDetailFormDTOS(bookingDetailFormDTOS)
                .build();
        return bookingFullHistoryDTO;
    }

    @Override
    public List<BookingDTO> getAllByStatusAndSitterId(String status, String sitterId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByStatusAndSitter_Id(status, sitterId);
        for (Booking booking : bookings) {
            SitterDTO sitterDTO = SitterDTO.builder()
                    .id(booking.getSitter().getId())
                    .fullName(booking.getSitter().getAccount().getFullName())
                    .phone(booking.getSitter().getAccount().getPhone())
                    .email(booking.getSitter().getAccount().getEmail())
                    .gender(booking.getSitter().getGender())
                    .status(booking.getSitter().getAccount().getStatus())
                    .build();
            CustomerDTO customerDTO = CustomerDTO.builder()
                    .id(booking.getCustomer().getId())
                    .fullName(booking.getCustomer().getAccount().getFullName())
                    .phone(booking.getCustomer().getAccount().getPhone())
                    .email(booking.getCustomer().getAccount().getEmail())
                    .address(booking.getCustomer().getAddress())
                    .gender(booking.getCustomer().getGender())
                    .image(booking.getCustomer().getAvatarImgUrl())
                    .status(booking.getCustomer().getAccount().getStatus())
                    .build();
            ElderDTO elderDTO = ElderDTO.builder()
                    .id(booking.getElder().getId())
                    .dob(booking.getElder().getDob())
                    .fullName(booking.getElder().getFullName())
                    .gender(booking.getElder().getGender())
                    .build();

            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .totalPrice(booking.getTotalPrice())
                    .elderDTO(elderDTO)
                    .latitude(booking.getLatitude())
                    .longitude(booking.getLongitude())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .customerDTO(customerDTO)
                    .sitterDTO(sitterDTO)
                    .build();
            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    @Override
    public List<SitterDTO> getAllByPackageBySitterStatus(String packageId, String status, String sitterID) {
        //filter sister due to package
        List<SitterPackage> sitterPackages = sitterPackageRepository.findAllBy_package_IdAndSitterProfile_Account_Status(packageId, status);
        List<SitterDTO> SitterDTOS = new ArrayList<>();
        for (SitterPackage sitterPackage : sitterPackages) {
            if (sitterPackage.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                SitterProfile sitterProfile = sitterProfileRepository.findById(sitterPackage.getSitterProfile().getId()).get();
                if (!sitterProfile.getAccount().getId().equals(sitterID)) {

                    SitterDTO sitterDTO = SitterDTO.builder()
                            .id(sitterProfile.getId())
                            .fullName(sitterProfile.getAccount().getFullName())
                            .phone(sitterProfile.getAccount().getPhone())
                            .email(sitterProfile.getAccount().getEmail())
                            .gender(sitterProfile.getGender())
                            .image(sitterProfile.getAvatarImgUrl())
                            .address(sitterProfile.getAddress())
                            .status(sitterProfile.getAccount().getStatus())
                            .build();
                    SitterDTOS.add(sitterDTO);
                }
            }
        }
        return SitterDTOS;
    }

    @Override
    public BookingFormDTO getBookingFormById(String bookingId) {
        BookingFormDTO bookingFormDTO = null;
//        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        Booking booking = bookingRepository.findById(bookingId).get();
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(bookingId);
        List<BookingDetailFormDTO> bookingDetailFormDTOS = new ArrayList<>();
        ElderDTO elderDTO = ElderDTO.builder()
                .id(booking.getElder().getId())
                .fullName(booking.getElder().getFullName())
                .gender(booking.getElder().getGender())
                .dob(booking.getElder().getDob())
                .build();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(booking.getCustomer().getId())
                .fullName(booking.getCustomer().getAccount().getFullName())
                .phone(booking.getCustomer().getAccount().getPhone())
                .email(booking.getCustomer().getAccount().getEmail())
                .address(booking.getCustomer().getAddress())
                .gender(booking.getCustomer().getGender())
                .status(booking.getCustomer().getAccount().getStatus())
                .build();

        for (BookingDetail bookingDetail : bookingDetails) {
            List<DetailServiceDTO> detailServiceDTOS = new ArrayList<>();

            List<DetailService> detailServices = bookingDetail.getDetailServices();
            for (DetailService detailService : detailServices) {
                DetailServiceDTO detailServiceDTO = DetailServiceDTO.builder()
                        .id(detailService.getId())
                        .serviceName(detailService.getServiceName())
                        .servicePrice(detailService.getServicePrice())
                        .serviceDuration(detailService.getServiceDuration())
                        .build();
                detailServiceDTOS.add(detailServiceDTO);
            }
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(bookingDetail.get_package().getId())
                    .name(bookingDetail.get_package().getName())
                    .price(bookingDetail.get_package().getPrice())
                    .duration(bookingDetail.get_package().getDuration())
                    .desc(bookingDetail.get_package().getDescription())
                    .build();

            BookingDetailFormDTO bookingDetailFormDTO = BookingDetailFormDTO.builder()
                    .id(bookingDetail.getId())
                    .estimateTime(bookingDetail.getEstimateTime())
                    .packageName(packageDTO.getName())
                    .startDateTime(bookingDetail.getStartDateTime())
                    .endDateTime(bookingDetail.getEndDateTime())
                    .packageDTO(packageDTO)
                    .detailServiceDTOS(detailServiceDTOS)
                    .build();
            bookingDetailFormDTOS.add(bookingDetailFormDTO);
        }
        bookingFormDTO = BookingFormDTO.builder()
                .id(booking.getId())
                .address(booking.getAddress())
                .createDate(booking.getCreateDate())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .deposit(booking.getDeposit())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .customerDTO(customerDTO)
                .elderDTO(elderDTO)
                .bookingDetailFormDTOS(bookingDetailFormDTOS)
                .build();
        return bookingFormDTO;
    }

    @Override
    public BookingFullDetailDTO getBookingFullDetail(String bookingId) {
        BookingFullDetailDTO bookingFullDetailDTO = null;
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(bookingId);
        List<BookingDetailFormDTO> bookingDetailFormDTOS = new ArrayList<>();
        SitterDTO sitterDTO = null;
        if (booking.getSitter() != null) {
            float rate = 0;
            if (booking.getSitter().getRate() != null) rate = booking.getSitter().getRate();
            int age = LocalDate.now(istZoneId).getYear() - booking.getSitter().getAccount().getSitterProfile().getDob().getYear();
            sitterDTO = SitterDTO.builder()
                    .id(booking.getSitter().getId())
                    .fullName(booking.getSitter().getAccount().getFullName())
                    .phone(booking.getSitter().getAccount().getPhone())
                    .email(booking.getSitter().getAccount().getEmail())
                    .age(age)
                    .gender(booking.getSitter().getGender())
                    .image(booking.getSitter().getAvatarImgUrl())
                    .rate(rate)
                    .status(booking.getSitter().getAccount().getStatus())
                    .build();
        }

        ElderDTO elderDTO = ElderDTO.builder()//
                .id(booking.getElder().getId())
                .fullName(booking.getElder().getFullName())
                .gender(booking.getElder().getGender())
                .dob(booking.getElder().getDob())
                .age(LocalDate.now(istZoneId).getYear() - booking.getElder().getDob().getYear())
                .healStatus(booking.getElder().getHealthStatus())
                .build();


        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(booking.getCustomer().getId())
                .fullName(booking.getCustomer().getAccount().getFullName())
                .phone(booking.getCustomer().getAccount().getPhone())
                .email(booking.getCustomer().getAccount().getEmail())
                .address(booking.getCustomer().getAddress())
                .age(LocalDate.now(istZoneId).getYear() - booking.getCustomer().getAccount().getCustomerProfile().getDob().getYear())
                .gender(booking.getCustomer().getGender())
                .image(booking.getCustomer().getAvatarImgUrl())
                .status(booking.getCustomer().getAccount().getStatus())
                .build();

        for (BookingDetail bookingDetail : bookingDetails) {
            List<DetailServiceDTO> detailServiceDTOS = new ArrayList<>();

            List<DetailService> detailServices = bookingDetail.getDetailServices();
            for (DetailService detailService : detailServices) {
                DetailServiceDTO detailServiceDTO = DetailServiceDTO.builder()
                        .id(detailService.getId())
                        .serviceName(detailService.getServiceName())
                        .servicePrice(detailService.getServicePrice())
                        .serviceDuration(detailService.getServiceDuration())
                        .build();
                detailServiceDTOS.add(detailServiceDTO);
            }
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(bookingDetail.get_package().getId())
                    .name(bookingDetail.get_package().getName())
                    .price(bookingDetail.get_package().getPrice())
                    .duration(bookingDetail.get_package().getDuration())
                    .desc(bookingDetail.get_package().getDescription())
                    .build();

            BookingDetailFormDTO bookingDetailFormDTO = BookingDetailFormDTO.builder()
                    .id(bookingDetail.getId())
                    .estimateTime(bookingDetail.getEstimateTime())
                    .packageName(packageDTO.getName())
                    .startDateTime(bookingDetail.getStartDateTime())
                    .endDateTime(bookingDetail.getEndDateTime())
                    .packageDTO(packageDTO)
                    .status(bookingDetail.getStatus())
                    .detailServiceDTOS(detailServiceDTOS)
                    .build();
            bookingDetailFormDTOS.add(bookingDetailFormDTO);
            bookingDetailFormDTOS.sort(Comparator.comparing(o -> o.getStartDateTime()));
        }
        String des = "";
        if (booking.getDescription() != null) des = booking.getDescription();
        bookingFullDetailDTO = BookingFullDetailDTO.builder()
                .id(booking.getId())
                .address(booking.getAddress())
                .createDate(booking.getCreateDate())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .latitude(booking.getLatitude())
                .longitude(booking.getLongitude())
                .deposit(booking.getDeposit())
                .status(booking.getStatus())
                .description(des)
                .totalPrice(booking.getTotalPrice())
                .customerDTO(customerDTO)
                .elderDTO(elderDTO)
                .sitterDTO(sitterDTO)
                .bookingDetailFormDTOS(bookingDetailFormDTOS)
                .build();
        return bookingFullDetailDTO;
    }

    @Override
    public List<BookingScheduleCustomerDTO> getAllBookingScheduleCustomer(String customerId) {
        List<BookingScheduleCustomerDTO> bookingScheduleCustomerDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerId);
        for (Booking booking : bookings) {
            if (!booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
                List<BookingDetail> bookingDetails = booking.getBookingDetails();
                String sitterName = null;
                if (booking.getSitter() != null) sitterName = booking.getSitter().getAccount().getFullName();
                for (BookingDetail bookingDetail : bookingDetails) {
                    BookingScheduleCustomerDTO bookingScheduleCustomerDTO = BookingScheduleCustomerDTO.builder()
                            .bookingId(booking.getId())
                            .bookingDetailId(bookingDetail.getId())
                            .sitterName(sitterName)
                            .startDateTime(bookingDetail.getStartDateTime())
                            .endDateTime(bookingDetail.getEndDateTime())
                            .bookingStatus(booking.getStatus())
                            .bookingDetailStatus(bookingDetail.getStatus())
                            .build();
                    bookingScheduleCustomerDTOS.add(bookingScheduleCustomerDTO);
                }
            }
        }
//        bookingScheduleCustomerDTOS.sort(Comparator.comparing(o -> o.getStartDateTime()));
        return bookingScheduleCustomerDTOS;
    }

    @Override
    public List<BookingDTO> getAllBookingInPresent(String sitterId) {
        List<Booking> bookings = bookingRepository.findAllBySitter_Id(sitterId);
        List<String> bookingIds = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(BookingStatus.ASSIGNED.toString()) || booking.getStatus().equals(BookingStatus.IN_PROGRESS.toString())) {
                List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(booking.getId());
                for (BookingDetail bookingDetail : bookingDetails) {
                    if (bookingDetail.getStartDateTime().toString().split("T")[0].equals(LocalDate.now(istZoneId).toString())) {
                        bookingIds.add(booking.getId());
                    }
                }
            }
        }
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        for (String id : bookingIds) {
            Booking booking = bookingRepository.findById(id).get();
            SitterDTO sitterDTO = SitterDTO.builder()
                    .id(booking.getSitter().getId())
                    .fullName(booking.getSitter().getAccount().getFullName())
                    .phone(booking.getSitter().getAccount().getPhone())
                    .email(booking.getSitter().getAccount().getEmail())
                    .gender(booking.getSitter().getGender())
                    .status(booking.getSitter().getAccount().getStatus())
                    .build();
            CustomerDTO customerDTO = CustomerDTO.builder()
                    .id(booking.getCustomer().getId())
                    .fullName(booking.getCustomer().getAccount().getFullName())
                    .phone(booking.getCustomer().getAccount().getPhone())
                    .email(booking.getCustomer().getAccount().getEmail())
                    .address(booking.getCustomer().getAddress())
                    .gender(booking.getCustomer().getGender())
                    .status(booking.getCustomer().getAccount().getStatus())
                    .build();
            ElderDTO elderDTO = ElderDTO.builder()
                    .id(booking.getElder().getId())
                    .dob(booking.getElder().getDob())
                    .fullName(booking.getElder().getFullName())
                    .gender(booking.getElder().getGender())
                    .build();

            BookingDTO bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .address(booking.getAddress())
                    .createDate(booking.getCreateDate())
                    .status(booking.getStatus())
                    .totalPrice(booking.getTotalPrice())
                    .elderDTO(elderDTO)
                    .endTime(booking.getEndTime())
                    .startTime(booking.getStartTime())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .customerDTO(customerDTO)
                    .sitterDTO(sitterDTO)
                    .build();
            bookingDTOS.add(bookingDTO);
        }

        return bookingDTOS;
    }

    @Override
    public List<BookingScheduleCustomerPresentDTO> getAllBookingScheduleCustomerPresent(String customerId) {
        List<BookingScheduleCustomerPresentDTO> bookingScheduleCustomerDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerId);
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(BookingStatus.ASSIGNED.toString())) {
                String sitterName = null;
                if (booking.getSitter() != null) sitterName = booking.getSitter().getAccount().getFullName();
                BookingScheduleCustomerPresentDTO bookingScheduleCustomerDTO = BookingScheduleCustomerPresentDTO.builder()
                        .bookingId(booking.getId())
                        .sitterName(sitterName)
                        .startDateTime(LocalDateTime.parse(booking.getStartDate() + "T" + booking.getStartTime()))
                        .endDateTime(LocalDateTime.parse(booking.getEndDate() + "T" + booking.getEndTime()))
                        .bookingStatus(booking.getStatus())
                        .elderName(booking.getElder().getFullName())
                        .build();
                bookingScheduleCustomerDTOS.add(bookingScheduleCustomerDTO);
            }
        }
        return bookingScheduleCustomerDTOS;
    }

    @Override
    public List<BookingScheduleSitterPresentDTO> getAllBookingScheduleSitterPresent(String sitterId) {
        List<BookingScheduleSitterPresentDTO> bookingScheduleSitterPresentDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllBySitter_Id(sitterId);
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(BookingStatus.ASSIGNED.toString())) {
                BookingScheduleSitterPresentDTO bookingScheduleSitterPresentDTO = BookingScheduleSitterPresentDTO.builder()
                        .bookingId(booking.getId())
                        .customerName(booking.getCustomer().getAccount().getFullName())
                        .startDateTime(LocalDateTime.parse(booking.getStartDate() + "T" + booking.getStartTime()))
                        .endDateTime(LocalDateTime.parse(booking.getEndDate() + "T" + booking.getEndTime()))
                        .bookingStatus(booking.getStatus())
                        .elderName(booking.getElder().getFullName())
                        .build();
                bookingScheduleSitterPresentDTOS.add(bookingScheduleSitterPresentDTO);
            }
        }
        return bookingScheduleSitterPresentDTOS;
    }

    @Override
    public List<BookingScheduleSitterDTO> getAllBookingScheduleSitter(String sitterId) {
        List<BookingScheduleSitterDTO> bookingScheduleSitterDTOS = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllBySitter_Id(sitterId);
        for (Booking booking : bookings) {
            if (booking.getStatus().equals(BookingStatus.IN_PROGRESS.toString()) ||
                    booking.getStatus().equals(BookingStatus.ASSIGNED.toString())) {
                List<BookingDetail> bookingDetails = booking.getBookingDetails();
                for (BookingDetail bookingDetail : bookingDetails) {
                    BookingScheduleSitterDTO bookingScheduleSitterDTO = BookingScheduleSitterDTO.builder()
                            .bookingId(booking.getId())
                            .bookingDetailId(bookingDetail.getId())
                            .customerName(booking.getCustomer().getAccount().getFullName())
                            .startDateTime(bookingDetail.getStartDateTime())
                            .endDateTime(bookingDetail.getEndDateTime())
                            .bookingStatus(booking.getStatus())
                            .bookingDetailStatus(bookingDetail.getStatus())
                            .build();
                    bookingScheduleSitterDTOS.add(bookingScheduleSitterDTO);
                }
            }
        }
        return bookingScheduleSitterDTOS;
    }

    private boolean isAvailableSitter(Set<WorkingTimeDTOV2> dateListCheck, Set<WorkingTimeDTOV2> dateListCheck2) {
        return dateListCheck2.containsAll(dateListCheck);
    }

    @Override
    @Transactional
    // phải check lại là tại thời điểm nó dc add zo thì nó đã có trong booking nào trong thời điểm đó khg
    public Boolean assignSitterIntoBooking(String sitterId, String bookingId) {
        SitterProfile sitter = sitterProfileRepository.findById(sitterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy chăm sóc viên nào cả"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));

        // kiểm tra xem trong booking này có sitter chưa
        if (booking.getStatus().equals(BookingStatus.PENDING.toString())) {
            booking.setSitter(sitter);
            notificationService.sendNotification(sitter.getId(), "Thông báo", "Bạn đã được phân công vào một đơn vui lòng vào ứng dụng để xem thêm thông tin");
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Đã có chăm sóc viên được phân công vào vào đơn  vui lòng vào ứng dụng để xem thêm thông tin ");
            booking.setStatus(BookingStatus.ASSIGNED.toString());
            bookingRepository.save(booking);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đơn hàng này đã có chăm sóc hơn");
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean checkIn(CheckInDTO checkInDTO) {
        Booking booking = bookingRepository.findById(checkInDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy đơn nào cả"));
        if (!booking.getStatus().equals(BookingStatus.IN_PROGRESS.toString())) {
            booking.setStatus(BookingStatus.IN_PROGRESS.toString());
        }
        BookingDetail bookingDetail = bookingDetailRepository.findDetail(checkInDTO.getBookingId(), LocalDate.now(istZoneId).getDayOfMonth(), LocalDate.now(istZoneId).getMonthValue(), LocalDate.now().getYear());
        if (bookingDetail == null)
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không được bắt đầu trước ngày làm việc");
        if (bookingDetail.getStatus().equals(BookingDetailStatus.WAITING.toString())) {
            Duration duration = Duration.between(LocalDateTime.now(istZoneId), bookingDetail.getStartDateTime());
            long minutes = duration.toMinutes();
//            if (minutes < 30 && minutes > -30) {
            bookingDetail.setStartDateTime(LocalDateTime.now(istZoneId));
            bookingDetail.setLocation(checkInDTO.getLocation());
            bookingDetail.setStatus(BookingDetailStatus.WORKING.toString());
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Chăm sóc viên đã đến nơi làm việc");
            bookingDetailRepository.save(bookingDetail);
//            check = true;
            Tracking tracking = Tracking.builder()
                    .time(LocalTime.now(istZoneId))
                    .note("Đã đến nơi làm việc")
                    .bookingDetail(bookingDetail)
                    .build();
            trackingRepository.save(tracking);
        }
////        String startDate = checkInDTO.getStartDateTime().split("T")[0];
//        if (booking.getStartDate().equals(LocalDate.now(istZoneId)))
//            booking.setStatus(BookingStatus.IN_PROGRESS.toString());
//        for (BookingDetail bookingDetail : bookingDetails) {
//            String startDateBookingDetail = bookingDetail.getStartDateTime().toLocalDate().toString();
//            if (LocalDate.now(istZoneId).toString().equals(startDateBookingDetail) && bookingDetail.getStatus().equals(BookingDetailStatus.WAITING.toString())) {
//                Duration duration = Duration.between(LocalDateTime.now(istZoneId), bookingDetail.getStartDateTime());
//                long minutes = duration.toMinutes();
////                if (minutes < 30 && minutes > -30) {
//                bookingDetail.setStartDateTime(LocalDateTime.now(istZoneId));
//                bookingDetail.setLocation(checkInDTO.getLocation());
//                bookingDetail.setStatus(BookingDetailStatus.WORKING.toString());
//                notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Chăm sóc viên đã đến nơi làm việc");
//                bookingDetailRepository.save(bookingDetail);
//                check = true;
//                Tracking tracking = Tracking.builder()
//                        .time(LocalTime.now(istZoneId))
//                        .note("Đã đến nơi làm việc")
//                        .bookingDetail(bookingDetail)
//                        .build();
//                trackingRepository.save(tracking);
//            }
//        }
//            if (!check)
//                throw new ResponseStatusException(HttpStatus.valueOf(400), "Không được check-in trước ngày làm việc");

        bookingRepository.save(booking);
        return true;
    }

    private void refundOneDay(BookingDetail bookingDetail) {
        Booking booking = bookingRepository.findById(bookingDetail.getBooking().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy đơn nào cả"));
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

    @Override
    public Boolean checkOut(CheckOutDTO checkOutDTO) {
        Boolean checkOut = false;
        Booking booking = bookingRepository.findById(checkOutDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy đơn nào cả"));
        LocalDateTime endDateL = LocalDateTime.now(istZoneId);
        BookingDetail bookingDetail = bookingDetailRepository.findDetail(checkOutDTO.getBookingId(), LocalDate.now(istZoneId).getDayOfMonth(), LocalDate.now(istZoneId).getMonthValue(), LocalDate.now().getYear());
        if (bookingDetail == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Chưa tới giờ để kết thúc công việc hoặc bạn chưa bắt đầu công việc");
        }

        if (bookingDetail.getStatus().equals(BookingDetailStatus.WORKING.toString())) {
            bookingDetail.setEndDateTime(endDateL);
            bookingDetail.setStatus(BookingDetailStatus.DONE.toString());
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Chăm sóc viên đã kết thúc công việc làm việc");
            bookingDetailRepository.save(bookingDetail);
            Tracking tracking = Tracking.builder()
                    .bookingDetail(bookingDetail)
                    .note("Đã kết thúc buổi làm việc")
                    .time(LocalTime.now(istZoneId))
                    .build();
            trackingRepository.save(tracking);
        }
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_IdAndStatus(checkOutDTO.getBookingId(), BookingDetailStatus.WAITING.toString());
        if (ValidateUtil.isNullOrEmpty(bookingDetails)) {
            booking.setStatus(BookingStatus.PAID.toString());
            paySitter(booking);
            bookingRepository.save(booking);

        }
        return true;
    }


    @Override
    public CheckAddDateDTO checkAddDate(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(booking.getId());
        if (bookingDetails.size() == 30)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ có thể thêm lịch cho những đơn có làm việc trở lên và có dưới 30 ngày làm việc");
        bookingDetails.sort(Comparator.comparing(b -> b.getStartDateTime()));
        List<String> dates = new ArrayList<>();
        for (BookingDetail _bookingDetail : bookingDetails) {
            Duration duration = Duration.between(LocalDateTime.now(istZoneId), _bookingDetail.getStartDateTime());
            long hour = duration.toHours();
            if (hour > 12) {
                dates.add(_bookingDetail.getStartDateTime().toString().split("T")[0]);
            }
        }
        CheckAddDateDTO checkAddDateDTO = CheckAddDateDTO.builder()
                .isAdd(true)
                .maxDate(bookingDetails.get(0).getStartDateTime().plusDays(28).toString().split("T")[0])
                .dates(dates)
                .build();
        return checkAddDateDTO;

    }

    @Override
    public CheckReduceDateDTO checkReduceDate(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy đơn nào cả"));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(booking.getId());
        if (bookingDetails.size() <= 5)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ có thể bớt lịch cho những đơn có trên 5 ngày làm việc trở lên");
        long total = 0;
        for (BookingDetail _bookingDetail : bookingDetails) {
            if (_bookingDetail.getStatus().equals(BookingDetailStatus.CANCEL.toString())) {
                total += 1;
            }
        }
        if (total >= (int) Math.floor(bookingDetails.size() * 0.2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số Ngày hủy đã hơn 20% tổng đơn, Nếu không cần lịch này nữa bạn có thể hủy cả đơn");
        }
        List<String> dates = new ArrayList<>();

        bookingDetails.sort(Comparator.comparing(b -> b.getStartDateTime()));
        for (BookingDetail _bookingDetail : bookingDetails) {
            Duration duration = Duration.between(LocalDateTime.now(istZoneId), _bookingDetail.getStartDateTime());
            long hour = duration.toHours();
            if (hour > 12) {
                dates.add(_bookingDetail.getStartDateTime().toString().split("T")[0]);
            }
        }
        CheckReduceDateDTO checkReduceDateDTO = CheckReduceDateDTO.builder()
                .dates(dates)
                .isReduce(true)
                .numberOfReduce((int) Math.floor(bookingDetails.size() * 0.2))
                .build();
        return checkReduceDateDTO;

    }

    @Override
    @Transactional
    public Boolean reduceBooking(ReduceBookingDateDTO reduceBookingDateDTO) {
        Booking booking = bookingRepository.findById(reduceBookingDateDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(reduceBookingDateDTO.getBookingId());
        List<String> dates = reduceBookingDateDTO.getDates();
        BigDecimal refundPrice = BigDecimal.valueOf(0);
        for (BookingDetail _bookingDetail : bookingDetails) {
            for (String date : dates) {
                if (_bookingDetail.getStartDateTime().toString().split("T")[0].equals(date)) {
                    _bookingDetail.setStatus(BookingDetailStatus.CANCEL.toString());
                    bookingDetailRepository.save(_bookingDetail);
                    refundPrice = refundPrice.add(_bookingDetail.getPrice());
                }
            }
        }

        Account account = accountRepository.findByRole_Name("ADMIN");
        Wallet walletCus = walletRepository.findById(booking.getCustomer().getId()).get();
        walletCus.setAmount(walletCus.getAmount().add(refundPrice));
        walletCus = walletRepository.save(walletCus);

        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletCus)
                .amount(refundPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .build();
        walletTransactionRepository.save(walletTransaction);

        Wallet walletAdmin = walletRepository.findById(account.getId()).get();
        walletAdmin.setAmount(walletAdmin.getAmount().subtract(refundPrice));
        walletAdmin = walletRepository.save(walletAdmin);

        Transaction walletTransactionAD = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletAdmin)
                .amount(refundPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .build();
        walletTransactionRepository.save(walletTransactionAD);

        notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Bạn đã hủy ngày thành công số tiền của những ngày đó đã được hoàn trả vào ví của bạn");

        return true;
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBookingForCustomer(BookingCancelDTO bookingCancelDTO) {
        Booking booking = bookingRepository.findById(bookingCancelDTO.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
        if (booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Booking đã cancel");
        }

        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(booking.getId());
        bookingDetails.sort(Comparator.comparing(BookingDetail::getStartDateTime));

        if (booking.getStatus().equals(BookingStatus.IN_PROGRESS.toString())) {
            for (BookingDetail bookingDetail : bookingDetails) {
                if (!bookingDetail.getStatus().equals(BookingDetailStatus.DONE.toString())) {
                    bookingDetail.setStatus(StatusCode.CANCEL.toString());
                    bookingDetailRepository.save(bookingDetail);
                }
            }
            booking.setReason(booking.getReason());
            booking.setCancelDate(LocalDate.now(istZoneId));
            booking.setStatus(StatusCode.CANCEL.toString());
            booking = bookingRepository.save(booking);
            paySitter(booking);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn không được hoàn tiền cọc");
            assigningService.stopAssignQueue(booking);
            return BookingMapper.INSTANCE.bookingConvertToDTO(booking);
        }

        if (!ValidateUtil.isValidDateTimeRange(LocalDateTime.now(istZoneId), bookingDetails.get(0).getStartDateTime())) {
            //Neu trong thoi gian da bat dau lam viec -> khong hoan coc
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn không được hoàn tiền cọc");
            notificationService.sendNotification(booking.getSitter().getId(), "Thông báo", "Lịch đặt đã bị khách hàng hủy bạn vui lòng vào ứng dụng để kiểm tra");
        }

        Duration duration = Duration.between(LocalDateTime.now(istZoneId), bookingDetails.get(0).getStartDateTime());
        long hour = duration.toHours();

        if (hour < 12) {
            //mất cọc
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn không được hoàn tiền cọc");
            notificationService.sendNotification(booking.getSitter().getId(), "Thông báo", "Lịch đặt đã bị khách hàng hủy bạn vui lòng vào ứng dụng để kiểm tra");
        } else if (hour <= 24) {
            refund(booking, 0.5f);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn được hoàn 50% tiền cọc");
            notificationService.sendNotification(booking.getSitter().getId(), "Thông báo", "Lịch đặt đã bị khách hàng hủy bạn vui lòng vào ứng dụng để kiểm tra");
        } else if (hour <= 36) {
            refund(booking, 0.7f);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn được hoàn 70% tiền cọc");
            notificationService.sendNotification(booking.getSitter().getId(), "Thông báo", "Lịch đặt đã bị khách hàng hủy bạn vui lòng vào ứng dụng để kiểm tra");
        } else {
            refund(booking, 1);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Hủy thành công bạn được hoàn 100% tiền cọc");
        }

        /// đếm ngày hủy của cus và đếm đủ 3 lần sẽ cảnh cáo đủ 7 lận sẽ ban
        CustomerProfile customerProfile = booking.getCustomer();
        customerProfile.setNumberOfCancels(ValidateUtil.isNullOrEmpty(customerProfile.getNumberOfCancels()) ? 1 : customerProfile.getNumberOfCancels() + 1);

        if (customerProfile.getNumberOfCancels() == 3) {
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Bạn đã hủy 3 lần lịch hẹn trong tháng nay, nếu tới 7 lần sẽ không được booking nữa");
        }
        if (customerProfile.getNumberOfCancels() == 7) {
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Bạn đã hủy 7 lần lịch hẹn trong tháng nay, bạn sẽ không được đặt booking trong tháng này nữa");
            customerProfile.getAccount().setStatus(StatusCode.DEACTIVATE.toString());
            customerProfile.getAccount().setDeviceId("");

            //deactive all booking belong to this customer profile
            cancelAllBookingCustomer(customerProfile.getId());
        }
        for (BookingDetail bookingDetail : bookingDetails) {
            bookingDetail.setStatus(StatusCode.CANCEL.toString());
        }
        booking.setStatus(BookingStatus.CANCEL.toString());
        booking.setBookingDetails(bookingDetails);
        booking.setCustomer(customerProfile);
        booking.setReason(booking.getReason());
        booking.setCancelDate(LocalDate.now(istZoneId));
        booking = bookingRepository.save(booking);

        assigningService.stopAssignQueue(booking);
        return BookingMapper.INSTANCE.bookingConvertToDTO(booking);
    }

    //system cancel
    public void cancelAllBookingCustomer(String customerID) {
        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerID);
        bookings.forEach(booking -> {
            paySitter(booking);
            booking.setStatus(BookingStatus.CANCEL.toString());
        });
        bookingRepository.saveAll(bookings);
    }

    public void paySitter(Booking booking) {
        Account account = accountRepository.findByRole_Name("ADMIN");
        Setting setting = settingRepository.findById("1").get();
        Wallet walletAdmin = walletRepository.findById(account.getId()).get();
        BigDecimal receive = BigDecimal.valueOf(0);
        List<BookingDetail> bookingDetailList = bookingDetailRepository.findAllByBooking_Id(booking.getId());
        for (BookingDetail bookingDetail : bookingDetailList) {
            if (bookingDetail.getStatus().equals(BookingDetailStatus.DONE.toString()))
                receive = receive.add(bookingDetail.getPrice());
        }
        System.out.println(receive);
        if (receive.compareTo(BigDecimal.valueOf(0)) != 0) {
            receive = receive.multiply(BigDecimal.valueOf(1 - setting.getCommission()));
            receive = receive.setScale(0, BigDecimal.ROUND_HALF_EVEN);
//            System.out.println(receive);
            walletAdmin.setAmount(walletAdmin.getAmount().subtract(receive));
            walletRepository.save(walletAdmin);
            Transaction transactionAD = Transaction.builder()
                    .type(TypeCode.REVENUE.toString())
                    .createDateTime(LocalDateTime.now(istZoneId))
                    .paymentMethod(PaymentMethod.WALLET.toString())
                    .amount(booking.getDeposit().subtract(receive))
                    .booking(booking)
                    .wallet(walletAdmin)
                    .build();
            walletTransactionRepository.save(transactionAD);

            Wallet walletSitter = walletRepository.findById(booking.getSitter().getId()).get();
            walletSitter.setAmount(walletSitter.getAmount().add(receive));
            walletRepository.save(walletSitter);

            Transaction transactionSit = Transaction.builder()
                    .type(TypeCode.RECEIVE.toString())
                    .createDateTime(LocalDateTime.now(istZoneId))
                    .paymentMethod(PaymentMethod.WALLET.toString())
                    .amount(receive)
                    .booking(booking)
                    .wallet(walletSitter)
                    .build();
            walletTransactionRepository.save(transactionSit);
            notificationService.sendNotification(walletSitter.getId(), "Thông báo", "Bạn đã nhận được tiền từ công viêc");
        }

    }

    private void refund(Booking booking, float percent) {
        Account account = accountRepository.findByRole_Name("ADMIN");
        Wallet walletCus = walletRepository.findById(booking.getCustomer().getId()).get();
        BigDecimal moneyRefund = booking.getDeposit().multiply(BigDecimal.valueOf(percent));
        walletCus.setAmount(walletCus.getAmount().add(moneyRefund));
        walletCus = walletRepository.save(walletCus);
        System.out.println(walletCus.getAmount());


        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletCus)
                .amount(moneyRefund)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(booking)
                .build();
        walletTransactionRepository.save(walletTransaction);

        Wallet walletAdmin = walletRepository.findById(account.getId()).get();
        walletAdmin.setAmount(walletAdmin.getAmount().subtract(moneyRefund));
        walletAdmin = walletRepository.save(walletAdmin);

        Transaction walletTransactionAD = Transaction.builder()
                .type(TypeCode.REFUND.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(walletAdmin)
                .amount(moneyRefund)
                .booking(booking)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .build();
        walletTransactionRepository.save(walletTransactionAD);
    }


//    @Override
//    public Boolean cancelBookingDetail(String bookingDetailId, String bookingId) {
//        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(bookingId);
//        int size = bookingDetails.size();
//        int slotCancel = 0;
//        for (BookingDetail bookingDetail : bookingDetails) {
//            if (bookingDetail.getStatus().equals(BookingDetailStatus.CANCEL.toString())) slotCancel = slotCancel + 1;
//        }
//        if (slotCancel > Math.round(size / (0.2)))
//            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không thể giảm bớt nữa, nếu không cần những lịch tiếp theo bạn có thể hủy cả booking");
//        BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).get();
//        bookingDetail.setStatus(StatusCode.CANCEL.toString());
//        bookingDetailRepository.save(bookingDetail);
//        return true;
//    }

    @Override
    public Boolean cancelBookingForSitter(BookingCancelDTO bookingCancelDTO) {
        Booking booking = bookingRepository.findById(bookingCancelDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
        if (LocalDateTime.now(istZoneId).plusHours(12).isBefore(LocalDateTime.parse(booking.getStartDate() + "T" + booking.getStartTime()))) {
            booking.setStatus(BookingStatus.CANCEL.toString());
            booking.setReason(booking.getReason());
            List<BookingDetail> bookingDetails = booking.getBookingDetails();
            for (BookingDetail bookingDetail : bookingDetails) {
                bookingDetail.setStatus(StatusCode.CANCEL.toString());
                bookingDetailRepository.save(bookingDetail);
            }

            booking.setCancelDate(LocalDate.now(istZoneId));
            bookingRepository.save(booking);
            refund(booking, 1);
            notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Chăm sóc viên đã hủy lịch của bạn, bạn được hoàn 100% tiền cọc");
            return true;
        } else
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể hủy vì đã bắt đầu ngày làm việc nếu có chuyện gì xin hãy gửi phản hồi cho hệ thống xử lý");
    }

//    @Override
//    public BookingCancelFormDTO getCancelFormBooking(String bookingId) {
//        BookingCancelFormDTO bookingCancelFormDTO = null;
//        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm  thấy booking nào cả"));
//        List<BookingDetail> bookingDetails = booking.getBookingDetails();
//        List<BookingDetailFormDTO> bookingDetailFormDTOS = new ArrayList<>();
//        SitterDTO sitterDTO = null;
//        CancelPersonDTO cancelPersonDTO = null;
//        if (booking.getSitter() != null) {
//            sitterDTO = SitterDTO.builder()
//                    .id(booking.getSitter().getId())
//                    .fullName(booking.getSitter().getAccount().getFullName())
//                    .phone(booking.getSitter().getAccount().getPhone())
//                    .email(booking.getSitter().getAccount().getEmail())
//                    .gender(booking.getSitter().getGender())
//                    .image(booking.getSitter().getAvatarImgUrl())
//                    .status(booking.getSitter().getAccount().getStatus())
//                    .build();
//        }
//        if (booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
//            cancelPersonDTO = CancelPersonDTO.builder()
//                    .id(booking.getSitter().getId())
//                    .fullName(booking.getSitter().getAccount().getFullName())
//                    .phone(booking.getSitter().getAccount().getPhone())
//                    .email(booking.getSitter().getAccount().getEmail())
//                    .address(booking.getSitter().getAddress())
//                    .gender(booking.getSitter().getGender())
//                    .status(booking.getSitter().getAccount().getStatus())
//                    .image(booking.getSitter().getAvatarImgUrl())
//                    .roleName(booking.getSitter().getAccount().getRole().getName())
//                    .build();
//        }
//        if (booking.getStatus().equals(BookingStatus.CANCEL.toString())) {
//            cancelPersonDTO = CancelPersonDTO.builder()
//                    .id(booking.getCustomer().getId())
//                    .fullName(booking.getCustomer().getAccount().getFullName())
//                    .phone(booking.getCustomer().getAccount().getPhone())
//                    .email(booking.getCustomer().getAccount().getEmail())
//                    .address(booking.getCustomer().getAddress())
//                    .gender(booking.getCustomer().getGender())
//                    .status(booking.getCustomer().getAccount().getStatus())
//                    .image(booking.getCustomer().getAvatarImgUrl())
//                    .roleName(booking.getCustomer().getAccount().getRole().getName())
//                    .build();
//        }
//
//        ElderDTO elderDTO = ElderDTO.builder()
//                .id(booking.getElder().getId())
//                .fullName(booking.getElder().getFullName())
//                .gender(booking.getElder().getGender())
//                .dob(booking.getElder().getDob())
//                .build();
//
//        CustomerDTO customerDTO = CustomerDTO.builder()
//                .id(booking.getCustomer().getId())
//                .fullName(booking.getCustomer().getAccount().getFullName())
//                .phone(booking.getCustomer().getAccount().getPhone())
//                .email(booking.getCustomer().getAccount().getEmail())
//                .address(booking.getCustomer().getAddress())
//                .gender(booking.getCustomer().getGender())
//                .status(booking.getCustomer().getAccount().getStatus())
//                .build();
//
//        for (BookingDetail bookingDetail : bookingDetails) {
//            List<DetailServiceDTO> detailServiceDTOS = new ArrayList<>();
//
//            List<DetailService> detailServices = bookingDetail.getDetailServices();
//            for (DetailService detailService : detailServices) {
//                DetailServiceDTO detailServiceDTO = DetailServiceDTO.builder()
//                        .id(detailService.getId())
//                        .serviceName(detailService.getServiceName())
//                        .servicePrice(detailService.getServicePrice())
//                        .serviceDuration(detailService.getServiceDuration())
//                        .build();
//                detailServiceDTOS.add(detailServiceDTO);
//            }
//            PackageDTO packageDTO = PackageDTO.builder()
//                    .id(bookingDetail.get_package().getId())
//                    .name(bookingDetail.get_package().getName())
//                    .price(bookingDetail.get_package().getPrice())
//                    .duration(bookingDetail.get_package().getDuration())
//                    .desc(bookingDetail.get_package().getDescription())
//                    .build();
//
//            BookingDetailFormDTO bookingDetailFormDTO = BookingDetailFormDTO.builder()
//                    .id(bookingDetail.getId())
//                    .estimateTime(bookingDetail.getEstimateTime())
//                    .packageName(packageDTO.getName())
//                    .startDateTime(bookingDetail.getStartDateTime())
//                    .endDateTime(bookingDetail.getEndDateTime())
//                    .packageDTO(packageDTO)
//                    .status(bookingDetail.getStatus())
//                    .detailServiceDTOS(detailServiceDTOS)
//                    .build();
//            bookingDetailFormDTOS.add(bookingDetailFormDTO);
//            bookingDetailFormDTOS.sort(Comparator.comparing(o -> o.getStartDateTime()));
//
//        }
//        bookingCancelFormDTO = BookingCancelFormDTO.builder()
//                .id(booking.getId())
//                .address(booking.getAddress())
//                .createDate(booking.getCreateDate())
//                .startDate(booking.getStartDate())
//                .endDate(booking.getEndDate())
//                .startTime(booking.getStartTime())
//                .endTime(booking.getEndTime())
//                .deposit(booking.getDeposit())
//                .status(booking.getStatus())
//                .totalPrice(booking.getTotalPrice())
//                .customerDTO(customerDTO)
//                .elderDTO(elderDTO)
//                .reason(booking.getReason())
//                .sitterDTO(sitterDTO)
//                .cancelPersonDTO(cancelPersonDTO)
//                .cancelDate(booking.getCancelDate())
//                .bookingDetailFormDTOS(bookingDetailFormDTOS)
//                .build();
//        return bookingCancelFormDTO;
//    }

    private BigDecimal caculatePercent(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Set<LocalDate> holidayDates) {
        BigDecimal percent = BigDecimal.valueOf(1L);
        LocalDate dateStart = dateTimeStart.toLocalDate();
        LocalDate dateEnd = dateTimeEnd.toLocalDate();

        //HOLIDAY
        if (holidayDates.contains(dateStart) || holidayDates.contains(dateEnd)) {
            return ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.2) : BigDecimal.valueOf(setting.getHoliday()).setScale(1, BigDecimal.ROUND_HALF_EVEN);
        }
        //WEEKEND
        if (dateStart.getDayOfWeek().toString().equals("SATURDAY") || dateStart.getDayOfWeek().toString().equals("SUNDAY")
                || dateEnd.getDayOfWeek().toString().equals("SATURDAY") || dateEnd.getDayOfWeek().toString().equals("SUNDAY")) {
            return ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.1) : BigDecimal.valueOf(setting.getWeekend()).setScale(1, BigDecimal.ROUND_HALF_EVEN);
        }
        //MIDNIGHT
        if (isMidNightSlot(dateTimeStart, dateTimeEnd)) {
            return ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.1) : BigDecimal.valueOf(setting.getMidnight()).setScale(1, BigDecimal.ROUND_HALF_EVEN);
        }
        return percent;
    }


    private boolean isMidNightSlot(LocalDateTime dateStart, LocalDateTime dateEnd) {
        Set<Integer> slot = getSlotBaseDateStartEnd(dateStart, dateEnd);
        Set<Integer> midNightslot = Stream.of(12, 1, 2, 3)
                .collect(Collectors.toCollection(HashSet::new));
        slot.retainAll(midNightslot);
        return !slot.isEmpty();
    }

    private Integer getSlotBaseDate(LocalDateTime dateStart) {

        for (Map.Entry<Integer, SlotDTOV2> map : slotSystem.entrySet()) {
            if (isTimeBetweenTwoTime(dateStart.toLocalTime(), map.getValue().getStartTime(), map.getValue().getEndTime())) {
                return map.getKey();
            }
        }
        return 0;
    }

    private LinkedHashSet<Integer> getSlotBaseDateStartEnd(LocalDateTime dateStart, LocalDateTime dateEnd) {
        LinkedHashSet<Integer> result = new LinkedHashSet<>();
        Integer startSlot = getSlotBaseDate(dateStart);
        while (dateStart.isBefore(dateEnd)) {
            dateStart = dateStart.plusHours(2);
            result.add(startSlot);
            startSlot++;
            if (startSlot > 12) {
                startSlot = 1;
            }
        }
        return result;
    }

    private String getDateSlotBookingDetail(LocalDateTime dateStart, Integer duration) {
        String result = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Integer startSlot = getSlotBaseDate(dateStart);
        LocalDateTime dateEnd = dateStart.plusHours(duration);
        while (dateStart.isBefore(dateEnd)) {
            String s = dateStart.toLocalDate().format(formatter) + "_" + startSlot + ";";
            result = result.concat(s);
            dateStart = dateStart.plusHours(2);
            startSlot++;
            if (startSlot > 12) {
                startSlot = 1;
            }
        }
        return result;
    }

    private boolean isTimeBetweenTwoTime(LocalTime dateTime, LocalTime starSlot, LocalTime endSlot) {
        return (dateTime.isAfter(starSlot) || dateTime.equals(starSlot)) && dateTime.isBefore(endSlot);
    }

    private Set<LocalDate> getListDate() {
        List<HolidayDate> holidayDates = holidayDateRepository.findAll();
        Set<LocalDate> localDateList = new HashSet<>();
        for (HolidayDate holidayDate : holidayDates) {
            LocalDate startDate = holidayDate.getStartDate();
            while (!startDate.isAfter(holidayDate.getEndDate())) {
                localDateList.add(startDate);
                startDate = startDate.plusDays(1);
            }
        }
        return localDateList;
    }

    private boolean checkOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        // Check for overlap
        return (start1.isEqual(start2) || start1.isBefore(end2)) && (end1.isEqual(end2) || end1.isAfter(start2));
    }


    public LocalDate getOverLapDateBookingDetailList(List<BookingDetail> bookingDetails) {
        for (int i = 0; i < bookingDetails.size() - 1; i++) {
            LocalDateTime start1 = bookingDetails.get(i).getStartDateTime();
            LocalDateTime end1 = bookingDetails.get(i).getEndDateTime();
            for (int j = i + 1; j < bookingDetails.size(); j++) {
                LocalDateTime start2 = bookingDetails.get(j).getStartDateTime();
                LocalDateTime end2 = bookingDetails.get(j).getEndDateTime();
                // Check for overlap
                if (checkOverlap(start1, end1, start2, end2)) {
                    return start1.toLocalDate();
                }
            }
        }
        return null; // No overlap found
    }

    private BigDecimal caculatePrice(LocalDate dateStart, LocalDate
            dateEnd, Set<Integer> slot, HashMap<PriceFactor, CheckPriceResponseDTO.DataDTO> mapDateFactor, BigDecimal
                                             pricePackage, Set<LocalDate> holidayDates) {
        //HOLIDAY
        if (holidayDates.contains(dateStart) || holidayDates.contains(dateEnd)) {
            return updateMapDateFactor(mapDateFactor, PriceFactor.HOLIDAY, ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.2) : BigDecimal.valueOf(setting.getHoliday()).setScale(1, BigDecimal.ROUND_HALF_EVEN), dateStart, pricePackage);
        }
        //WEEKEND
        if (dateStart.getDayOfWeek().toString().equals("SATURDAY") || dateStart.getDayOfWeek().toString().equals("SUNDAY")) {
            return updateMapDateFactor(mapDateFactor, PriceFactor.WEEKEND, ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.1) : BigDecimal.valueOf(setting.getWeekend()).setScale(1, BigDecimal.ROUND_HALF_EVEN), dateStart, pricePackage);
        }
        //MIDNIGHT
        if (isMidNightSlot(slot)) {
            return updateMapDateFactor(mapDateFactor, PriceFactor.MIDNIGHT, ValidateUtil.isNullOrEmpty(setting) ? BigDecimal.valueOf(1.1) : BigDecimal.valueOf(setting.getMidnight()).setScale(1, BigDecimal.ROUND_HALF_EVEN), dateStart, pricePackage);
        }

        return updateMapDateFactor(mapDateFactor, PriceFactor.NORMAL, BigDecimal.valueOf(1), dateStart, pricePackage);

    }

    private BigDecimal updateMapDateFactor(HashMap<PriceFactor, CheckPriceResponseDTO.DataDTO> map,
                                           PriceFactor factor, BigDecimal percent, LocalDate date,
                                           BigDecimal price) {
        price = price.multiply(percent);
        price = price.setScale(0, BigDecimal.ROUND_HALF_EVEN);
        if (ValidateUtil.isNullOrEmpty(map.get(factor))) {
            map.put(factor, CheckPriceResponseDTO.DataDTO.builder()
                    .date(1)
                    .percent(percent)
                    .price(price)
                    .build());
        } else {
            CheckPriceResponseDTO.DataDTO dto = map.get(factor);
            dto.setDate(dto.getDate() + 1);
            dto.setPrice(dto.getPrice().add(price));
            map.put(factor, dto);
        }
        return price;
    }

    private boolean isMidNightSlot(Set<Integer> slot) {
        Set<Integer> midNightslot = Stream.of(12, 1, 2, 3)
                .collect(Collectors.toCollection(HashSet::new));
        slot.retainAll(midNightslot);
        return !slot.isEmpty();
    }

    @Override
    public CheckPriceResponseDTO checkDateRangePrice(CheckPriceRequestDTO checkPriceRequestDTO) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        HashMap<PriceFactor, CheckPriceResponseDTO.DataDTO> mapData = new HashMap<>();
        Package packageEntity = packageRepository.findById(checkPriceRequestDTO.getPackageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PACKAGE));
        setting = settingRepository.findById("1")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_SETTING));
        Set<Integer> slot = new HashSet<>();
        Set<LocalDate> holidayDates = getListDate();

        for (LocalDate date : checkPriceRequestDTO.getDate()) {//
            if (ValidateUtil.isNullOrEmpty(slot)) {
                LocalDateTime startDateTime = LocalDateTime.of(date, checkPriceRequestDTO.getStartTime());
                LocalDateTime endDateTime = startDateTime.plusHours(packageEntity.getDuration());


                while (startDateTime.isBefore(endDateTime)) {
                    slot.addAll(getSlotBaseDateStartEnd(startDateTime, endDateTime));
                    startDateTime = startDateTime.plusHours(2);
                }
            }
            BigDecimal priceDate = caculatePrice(date, date.plusDays(1), slot, mapData, packageEntity.getPrice(), holidayDates);
            totalPrice = totalPrice.add(priceDate);
        }

        //check promo
        if (!ValidateUtil.isNullOrEmpty(checkPriceRequestDTO.getPromotion())) {
            Promotion promotion = promotionRepository.findById(checkPriceRequestDTO.getPromotion())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PROMOTION));
            LocalDate dateNow = LocalDate.now(istZoneId);
            if (dateNow.isAfter(promotion.getEndDate()) || dateNow.isBefore(promotion.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_INVALID_PROMOTION);
            }
            BigDecimal discountPrice = totalPrice.multiply(BigDecimal.valueOf(promotion.getValue()));
            discountPrice = discountPrice.setScale(0, BigDecimal.ROUND_HALF_EVEN);

            return CheckPriceResponseDTO.builder()
                    .discountPrice(discountPrice)
                    .afterDiscountPrice(totalPrice.subtract(discountPrice))
                    .datePriceInform(mapData)
                    .totalPrice(totalPrice)
                    .build();
        }

        return CheckPriceResponseDTO.builder()
                .datePriceInform(mapData)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    public BookingResponseDTO changeSitter(String bookingID) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_BOOKING));
        Booking newBooking = changeSitterTransactional(booking);
        List<SitterProfile> listSitter = getAvailableSitter(newBooking, newBooking.getDistrict(), booking.getSitter().getId());
        assigningService.assignSitter(newBooking, listSitter);
        return BookingMapper.INSTANCE.bookingConvertToDTO(newBooking);
    }

    @Transactional
    public Booking changeSitterTransactional(Booking booking) {
        Booking newBooking = null;
        //validate
        if (!booking.getStatus().equals(BookingStatus.ASSIGNED.toString())
                &&
                !booking.getStatus().equals(BookingStatus.IN_PROGRESS.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_STATUS_BOOKING);
        }
        if (booking.getNumOfChange() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_CHANGE_BOOKING);
        }

        List<BookingDetail> bookingDetails = booking.getBookingDetails();

        List<BookingDetail> bookingDetailsWorking = new ArrayList<>();
        List<BookingDetail> bookingDetailsWaiting = new ArrayList<>();
        List<BookingDetail> bookingDetailsDone = new ArrayList<>();

        for (BookingDetail bookingDetail : bookingDetails) {
            switch (bookingDetail.getStatus()) {
                case "WAITING":
                    bookingDetailsWaiting.add(bookingDetail);
                    break;
                case "DONE":
                    bookingDetailsDone.add(bookingDetail);
                    break;
                case "WORKING":
                    bookingDetailsWorking.add(bookingDetail);
                    break;
            }
        }

        //validate if exist booking detail working -> not change sitter
        if (!ValidateUtil.isNullOrEmpty(bookingDetailsWorking)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_STATUS_BOOKING);
        }

        try {
            //create new booking
            if (!ValidateUtil.isNullOrEmpty(bookingDetailsWaiting)) {
                BigDecimal totalPrice = BigDecimal.ZERO;
                String slotBooking = "";

                bookingDetailsWaiting.sort(Comparator.comparing(b -> b.getStartDateTime()));
                //validate if first date of list < 12 hour compare to today -> not change sitter
                Duration duration = Duration.between(LocalDateTime.now(istZoneId), bookingDetailsWaiting.get(0).getStartDateTime());
                if (duration.toHours() < 12) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DATE_TIME_BOOKING_CHANGE_SITTER);
                }

                LocalDateTime dateStart = bookingDetailsWaiting.get(0).getStartDateTime();

                newBooking = Booking.builder()
                        .address(booking.getAddress())
                        .createDate(LocalDate.now(istZoneId))
                        .startDate(dateStart.toLocalDate())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime())
                        .endDate(booking.getEndDate())
                        .description(booking.getDescription())
                        .status(BookingStatus.PENDING.toString())
                        .elder(booking.getElder())
                        .customer(booking.getCustomer())
                        .build();

                List<BookingDetail> addBookingDetailList = new ArrayList<>();
                for (BookingDetail bookingDetail : bookingDetailsWaiting) {

                    BookingDetail newBookingDetail = BookingDetail
                            .builder()
                            .booking(newBooking)
                            .startDateTime(bookingDetail.getStartDateTime())
                            .endDateTime(bookingDetail.getEndDateTime())
                            .estimateTime(bookingDetail.get_package().getDuration())
                            .packageName(bookingDetail.get_package().getName())
                            .price(bookingDetail.getPrice())
                            ._package(bookingDetail.get_package())
                            .status(BookingDetailStatus.WAITING.toString())
                            .percentChange(bookingDetail.getPercentChange())
                            .build();

                    List<DetailService> addList = new ArrayList<>();
                    for (PackageService packageService : bookingDetail.get_package().getPackageServices()) {
                        capstone.elsv2.entities.Service service = serviceRepository.findById(packageService.getService().getId()).get();
                        DetailService detailService = DetailService.builder()
                                .serviceDuration(service.getDuration())
                                .servicePrice(service.getPrice())
                                .serviceName(service.getName())
                                .bookingDetail(bookingDetail)
                                .build();
                        addList.add(detailService);
                    }
                    slotBooking = slotBooking.concat(getDateSlotBookingDetail(dateStart, bookingDetail.get_package().getDuration()));
                    totalPrice = totalPrice.add(bookingDetail.getPrice());
                    newBookingDetail.setDetailServices(addList);
                    newBooking.setDeposit(totalPrice);
                    addBookingDetailList.add(newBookingDetail);
                }
                newBooking.setNumOfChange(1);
                newBooking.setTotalPrice(totalPrice);
                newBooking.setSlots(slotBooking);
                newBooking.setBookingDetails(addBookingDetailList);
                newBooking.setDeposit(totalPrice);
                newBooking.setDistrict(booking.getDistrict());
                newBooking.setLatitude(booking.getLatitude());
                newBooking.setLongitude(booking.getLongitude());

//            find sitter -> if sitter not found not create booking
                List<SitterProfile> sitterProfiles = getAvailableSitter(newBooking, newBooking.getDistrict(), booking.getSitter().getId());
                if (ValidateUtil.isNullOrEmpty(sitterProfiles)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_SITTER);
                }

                //setCancel old Booking and pay money
                paySitter(booking);
                bookingDetails.forEach(bookingDetail -> {
                    if (bookingDetail.getStatus().equals(BookingDetailStatus.WAITING.toString())) {
                        bookingDetail.setStatus(BookingDetailStatus.CANCEL.toString());
                    }
                });
                booking.setBookingDetails(bookingDetails);
                booking.setCancelDate(LocalDate.now(istZoneId));
                booking.setStatus(BookingStatus.PAID.toString());

                bookingRepository.save(booking);
                newBooking = bookingRepository.save(newBooking);

            }

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getReason());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return newBooking;
    }

    @Override
    public BookingResponseDTO addDateBooking(AddDateBookingDTO dto) {
        Booking oldBooking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_BOOKING));
        Booking booking = addDateBooking(oldBooking, dto);
        if (ValidateUtil.isNullOrEmpty(booking.getSitter())) {
            List<SitterProfile> listSitter = getAvailableSitter(booking, booking.getDistrict(), null);
            assigningService.assignSitter(booking, listSitter);
        }
        return BookingMapper.INSTANCE.bookingConvertToDTO(booking);
    }

    @Transactional(rollbackOn = Exception.class)
    public Booking addDateBooking(Booking oldBooking, AddDateBookingDTO dto) {
        Booking newBooking = null;

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        Package packageEntity = oldBooking.getBookingDetails().get(0).get_package();

        Account customer = accountRepository.findById(oldBooking.getCustomer().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_CUSTOMER));

        if (customer.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DEACTIVATE_CUSTOMER);
        }

        if (dto.getEndDate().isBefore(dto.getStartDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_END_DATE_BEFORE_START_DATE);


        //validate booking start before 2 day current
        if (LocalDateTime.now(istZoneId).plusHours(24).isAfter(LocalDateTime.of(dto.getStartDate(), oldBooking.getStartTime()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_BOOKING_DATE_START_MUST_BEFORE_CURRENT_TWO_DAY);
        }

        //check duplicate booking package
        //get all booking detail of elder from date to date to check time duplicate
        List<BookingDetail> bookingDetailList = bookingDetailRepository.getListBookingDetailToValidDateTime(oldBooking.getElder().getId(), dto.getStartDate());

        List<AddBookingDetailDTO> addBookingDetailDTOList = new ArrayList<>();

        //TODO: checkListDate and in dateStart-> dateEnd range

        for (LocalDate date : dto.getDate()) {
            LocalDateTime dateTimeStart = LocalDateTime.of(date, oldBooking.getStartTime());
            LocalDateTime dateTimeEnd = dateTimeStart.plusHours(packageEntity.getDuration());
            addBookingDetailDTOList.add(new AddBookingDetailDTO(dateTimeStart, dateTimeEnd));
            while (dateTimeStart.isBefore(dateTimeEnd)) {
                bookingDetailList.add(new BookingDetail(dateTimeStart, dateTimeStart.plusHours(2)));
                dateTimeStart = dateTimeStart.plusHours(2);
            }
        }


        //check time duplicate
        if (bookingDetailList.size() > 1) {
            LocalDate overlapDate = getOverLapDateBookingDetailList(bookingDetailList);
            if (!ValidateUtil.isNullOrEmpty(overlapDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_OVERLAP_DATE_TIME_BOOKING + overlapDate);
            }
        }

        String slotBooking = "";
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        List<BookingDetail> listBookingDetail = new ArrayList<>();
        List<DetailService> detailServiceList = new ArrayList<>();

        for (PackageService packageService : packageEntity.getPackageServices()) {
            capstone.elsv2.entities.Service service = serviceRepository.findById(packageService.getService().getId()).get();
            DetailService detailService = DetailService.builder()
                    .serviceDuration(service.getDuration())
                    .servicePrice(service.getPrice())
                    .serviceName(service.getName())
                    .build();
            detailServiceList.add(detailService);
        }


        newBooking = Booking.builder()
                .address(oldBooking.getAddress())
                .createDate(LocalDate.now(istZoneId))
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(oldBooking.getDescription())
                .status(BookingStatus.PENDING.toString())
                .elder(oldBooking.getElder())
                .customer(customer.getCustomerProfile())
                .build();
        LocalDateTime dateStart = null;
        LocalDateTime dateEnd = null;
        Set<LocalDate> holidayDates = getListDate();
        for (AddBookingDetailDTO addBookingDetailDTO : addBookingDetailDTOList) {
            dateStart = addBookingDetailDTO.getStartDateTime();
            dateEnd = addBookingDetailDTO.getEndDateTime();

            //caculate price
            //TODO: dateEnd base slot end
            BigDecimal percentChange = caculatePercent(dateStart, dateEnd, holidayDates);
            BigDecimal priceEachBookingDetail = packageEntity.getPrice().multiply(percentChange);

            BookingDetail bookingDetail = BookingDetail.builder()
                    .booking(newBooking)
                    .startDateTime(dateStart)
                    .endDateTime(dateEnd)
                    .estimateTime(packageEntity.getDuration())
                    .packageName(packageEntity.getName())
                    .price(priceEachBookingDetail)
                    ._package(packageEntity)
                    .status(BookingDetailStatus.WAITING.toString())
                    .percentChange(percentChange)
                    .build();

            totalPrice = totalPrice.add(priceEachBookingDetail);
            slotBooking = slotBooking.concat(getDateSlotBookingDetail(dateStart, packageEntity.getDuration()));

            detailServiceList.forEach(detailService -> detailService.setBookingDetail(bookingDetail));
            List<DetailService> addList = new ArrayList<>();
            for (DetailService detailService : detailServiceList) {
                DetailService d = DetailService.builder()
                        .serviceDuration(detailService.getServiceDuration())
                        .servicePrice(detailService.getServicePrice())
                        .serviceName(detailService.getServiceName())
                        .bookingDetail(bookingDetail)
                        .build();
                addList.add(d);
            }
            bookingDetail.setDetailServices(addList);
            listBookingDetail.add(bookingDetail);
        }
        newBooking.setStartTime(dateStart.toLocalTime());
        newBooking.setEndTime(dateEnd.toLocalTime());
        newBooking.setBookingDetails(listBookingDetail);
        newBooking.setSlots(slotBooking);
        newBooking.setTotalPrice(totalPrice);
        newBooking.setDeposit(totalPrice);
        newBooking.setLatitude(oldBooking.getLatitude());
        newBooking.setLongitude(oldBooking.getLongitude());
        newBooking.setDistrict(oldBooking.getDistrict());

//      1. get old sitter -> if old sitter suitable -> set old sitter
//      2. if not get available sitter
        if (assigningService.isAvailableSitter(oldBooking.getSitter().getId(), newBooking)) {
            newBooking.setSitter(oldBooking.getSitter());
            newBooking.setStatus(BookingStatus.ASSIGNED.toString());
        } else {
            List<SitterProfile> listSitter = getAvailableSitter(newBooking, oldBooking.getDistrict(), null);
            if (ValidateUtil.isNullOrEmpty(listSitter)) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_NOT_FOUND_SITTER);
            }
        }

        Wallet customerWallet = walletRepository.findById(customer.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_WALLET)
        );
        if (customerWallet.getAmount().compareTo(totalPrice) < 0) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_WALLET + currencyVN.format(totalPrice.subtract(customerWallet.getAmount())));
        }

        newBooking = bookingRepository.save(newBooking);

        customerWallet.setAmount(customerWallet.getAmount().subtract(totalPrice));
        walletRepository.save(customerWallet);
        Transaction walletTransaction = Transaction.builder()
                .type(TypeCode.DEPOSIT.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(customerWallet)
                .amount(totalPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(newBooking)
                .build();
        walletTransactionRepository.save(walletTransaction);
        Account admin = accountRepository.findByRole_Name("ADMIN");
        Wallet adminWallet = walletRepository.findById(admin.getId()).get();
        adminWallet.setAmount(adminWallet.getAmount().add(totalPrice));
        walletRepository.save(adminWallet);

        Transaction walletTransactionAdmin = Transaction.builder()
                .type(TypeCode.DEPOSIT.toString())
                .createDateTime(LocalDateTime.now(istZoneId))
                .wallet(adminWallet)
                .amount(totalPrice)
                .paymentMethod(PaymentMethod.WALLET.toString())
                .booking(newBooking)
                .build();
        walletTransactionRepository.save(walletTransactionAdmin);

        return newBooking;
    }

    @Override
    public String checkDate(String bookingId) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllByBooking_Id(bookingId);
        for (BookingDetail bookingDetail : bookingDetails) {
            if (LocalDate.now(istZoneId).equals(bookingDetail.getStartDateTime().toLocalDate()) && bookingDetail.getStatus().equals(BookingDetailStatus.WAITING.toString())) {
                return "CHECK_IN";
            }
            if (LocalDate.now(istZoneId).equals(bookingDetail.getStartDateTime().toLocalDate()) && bookingDetail.getStatus().equals(BookingDetailStatus.WORKING.toString())) {
                return "CHECK_OUT";
            }
        }
        return "NONE";
    }

    @Override
    public Boolean cancelBooking(Booking booking) {
        if (!booking.getStatus().equalsIgnoreCase(BookingStatus.PENDING.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status không phải pending");
        }
        List<BookingDetail> bookingDetails = booking.getBookingDetails();

        for (BookingDetail bookingDetail : bookingDetails) {
            bookingDetail.setStatus(StatusCode.CANCEL.toString());
        }
        booking.setStatus(BookingStatus.CANCEL.toString());
        booking.setBookingDetails(bookingDetails);
        booking.setReason(booking.getReason());
        booking.setCancelDate(LocalDate.now(istZoneId));
        booking = bookingRepository.save(booking);

        refund(booking, 1);
        notificationService.sendNotification(booking.getCustomer().getId(),
                "Thông báo",
                "Booking bị hủy vì không tìm thấy sitter phù hợp, bạn được hoàn 100% tiền cọc");
        return true;
    }

    @Override
    public List<SitterProfile> getAvailableSitter(Booking booking, String district, String sitterId) {
        List<SitterProfile> sitterProfiles = getAllActivateSitterByPackageZone(booking.getBookingDetails().get(0).get_package().getId(), district);

        if (!ValidateUtil.isNullOrEmpty(sitterId)) {
            sitterProfiles = sitterProfiles.stream().filter(x -> !x.getId().equalsIgnoreCase(sitterId)).collect(Collectors.toList());
        }

        if (ValidateUtil.isNullOrEmpty(sitterProfiles)) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_NOT_FOUND_SITTER);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Set<DateRangeDTO> dateBookingDetailSet = new HashSet<>(); //list compare to booking time in which sitter assigned
        Set<WorkingTimeDTOV2> listBookingWorking = new HashSet<>(); //list compare to working time sitter
        for (String x : booking.getSlots().split(";")) {
            String date = x.split("_")[0];
            int slot = Integer.parseInt(x.split("_")[1]);
            LocalDate dateBooking = LocalDate.parse(date, formatter);

            LocalDateTime dateTimeStart = LocalDateTime.of(dateBooking, slotSystem.get(slot).getStartTime());
            LocalDateTime dateTimeEnd = LocalDateTime.of(dateBooking, slotSystem.get(slot).getEndTime());
            DateRangeDTO dateRangeDTO = new DateRangeDTO(dateTimeStart, dateTimeEnd, slot);
            dateBookingDetailSet.add(dateRangeDTO);
            listBookingWorking.add(new WorkingTimeDTOV2(dateRangeDTO.getStartTime().getDayOfWeek().toString(), dateRangeDTO.getSlot()));
        }

        return sitterProfiles.stream().filter(sitter -> {
            //get Booking sitter assigned
            List<BookingScheduleSitterDTO> listBookingSchedule = getAllBookingScheduleSitter(sitter.getId());

            Set<DateRangeDTO> dateSitterBookingSet = new HashSet<>();
            for (BookingScheduleSitterDTO scheduleSitterDTO : listBookingSchedule) {
                LocalDateTime dateTimeStart = scheduleSitterDTO.getStartDateTime();
                LocalDateTime dateTimeEnd = scheduleSitterDTO.getEndDateTime();
                while (dateTimeStart.isBefore(dateTimeEnd)) {
                    dateSitterBookingSet.add(new DateRangeDTO(dateTimeStart, dateTimeStart.plusHours(2), getSlotBaseDate(dateTimeStart)));
                    dateTimeStart = dateTimeStart.plusHours(2);
                }
            }

            //check time booking sitter assignedr overlap list booking
            if (ValidateUtil.isOverlap(new HashSet<>(dateBookingDetailSet), dateSitterBookingSet)) {
                return false;
            }

            //get list working time
            List<WorkingTimeDTO> listSitterWorking = workingTimeService.getAllActivateWorkingTime(sitter.getId());
            Set<WorkingTimeDTOV2> listSitterWorkingCheck = new HashSet<>();
            for (WorkingTimeDTO workingTime : listSitterWorking) {
                if (workingTime.getSlots() == null || workingTime.getSlots().isEmpty()) {
                    break;
                }
                List<String> listSlot = List.of(workingTime.getSlots().split("-"));
                for (String slot : listSlot) {
                    listSitterWorkingCheck.add(new WorkingTimeDTOV2(workingTime.getDayOfWeek(), Integer.parseInt(slot)));
                }
            }

            if (listSitterWorkingCheck.isEmpty()) {
                return false;
            }

            //filter list Working time with listSlot
            return isAvailableSitter(new HashSet<>(listBookingWorking), listSitterWorkingCheck);
        }).collect(Collectors.toList());
    }

    @Override
    public Booking getBooking(String bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
    }

    private List<SitterProfile> getAllActivateSitterByPackageZone(String packageId, String district) {
        //filter sister due to package
        return sitterProfileRepository.getActivateSitterByPackageAndZone(packageId, district);
    }

    @Transactional()
    @Override
    public Booking bookingWithoutWallet(AddBookingV2DTO addBookingDTO) {
        Booking booking = null;

        Account customer = accountRepository.findById(addBookingDTO.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_CUSTOMER));

        if (customer.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DEACTIVATE_CUSTOMER);
        }

        Elder elder = elderRepository.findById(addBookingDTO.getElderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_ELDER));

        Package packageEntity = packageRepository.findById(addBookingDTO.getPackageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PACKAGE));

        if (addBookingDTO.getAddress().equals("") && customer.getCustomerProfile().getAddress() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DEFAULT_ADDRESS);
        }

        if (addBookingDTO.getEndDate().isBefore(addBookingDTO.getStartDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_END_DATE_BEFORE_START_DATE);


        //validate booking start before 2 day current
        if (LocalDate.now(istZoneId).plusDays(2).isAfter(addBookingDTO.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_BOOKING_DATE_START_MUST_BEFORE_CURRENT_TWO_DAY);
        }

        //check duplicate booking package
        //get all booking detail of elder from date to date to check time duplicate
        List<BookingDetail> bookingDetailList = bookingDetailRepository.getListBookingDetailToValidDateTime(addBookingDTO.getElderId(), addBookingDTO.getStartDate());

        List<AddBookingDetailDTO> addBookingDetailDTOList = new ArrayList<>();

        for (LocalDate date : addBookingDTO.getDates()) {
            LocalDateTime dateTimeStart = LocalDateTime.of(date, addBookingDTO.getStartTime());
            LocalDateTime dateTimeEnd = dateTimeStart.plusHours(packageEntity.getDuration());
            addBookingDetailDTOList.add(new AddBookingDetailDTO(dateTimeStart, dateTimeEnd));
            while (dateTimeStart.isBefore(dateTimeEnd)) {
                bookingDetailList.add(new BookingDetail(dateTimeStart, dateTimeStart.plusHours(2)));
                dateTimeStart = dateTimeStart.plusHours(2);
            }
        }

        //check time duplicate
        if (bookingDetailList.size() > 1) {
            LocalDate overlapDate = getOverLapDateBookingDetailList(bookingDetailList);
            if (!ValidateUtil.isNullOrEmpty(overlapDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_OVERLAP_DATE_TIME_BOOKING + overlapDate);
            }
        }

        String slotBooking = "";
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        List<BookingDetail> listBookingDetail = new ArrayList<>();
        List<DetailService> detailServiceList = new ArrayList<>();

        for (PackageService packageService : packageEntity.getPackageServices()) {
            capstone.elsv2.entities.Service service = serviceRepository.findById(packageService.getService().getId()).get();
            DetailService detailService = DetailService.builder()
                    .serviceDuration(service.getDuration())
                    .servicePrice(service.getPrice())
                    .serviceName(service.getName())
                    .build();
            detailServiceList.add(detailService);
        }


        booking = Booking.builder()
                .address(addBookingDTO.getAddress())
                .createDate(LocalDate.now(istZoneId))
                .latitude(addBookingDTO.getLatitude())
                .Longitude(addBookingDTO.getLongitude())
                .startDate(addBookingDTO.getStartDate())
                .endDate(addBookingDTO.getEndDate())
                .description(addBookingDTO.getDescription())
                .status(BookingStatus.PENDING.toString())
                .elder(elder)
                .customer(customer.getCustomerProfile())
                .build();
        LocalDateTime dateStart = null;
        LocalDateTime dateEnd = null;
        Set<LocalDate> holidayDates = getListDate();
        for (AddBookingDetailDTO addBookingDetailDTO : addBookingDetailDTOList) {
            dateStart = addBookingDetailDTO.getStartDateTime();
            dateEnd = addBookingDetailDTO.getEndDateTime();

            //caculate price
            //TODO: dateEnd base slot end
            BigDecimal percentChange = caculatePercent(dateStart, dateEnd, holidayDates);
            BigDecimal priceEachBookingDetail = packageEntity.getPrice().multiply(percentChange);

            BookingDetail bookingDetail = BookingDetail.builder()
                    .booking(booking)
                    .startDateTime(dateStart)
                    .endDateTime(dateEnd)
                    .estimateTime(packageEntity.getDuration())
                    .packageName(packageEntity.getName())
                    .price(priceEachBookingDetail)
                    ._package(packageEntity)
                    .status(BookingDetailStatus.WAITING.toString())
                    .percentChange(percentChange)
                    .build();

            totalPrice = totalPrice.add(priceEachBookingDetail);
            slotBooking = slotBooking.concat(getDateSlotBookingDetail(dateStart, packageEntity.getDuration()));

            detailServiceList.forEach(detailService -> detailService.setBookingDetail(bookingDetail));
            List<DetailService> addList = new ArrayList<>();
            for (DetailService detailService : detailServiceList) {
                DetailService d = DetailService.builder()
                        .serviceDuration(detailService.getServiceDuration())
                        .servicePrice(detailService.getServicePrice())
                        .serviceName(detailService.getServiceName())
                        .bookingDetail(bookingDetail)
                        .build();
                addList.add(d);
            }
            bookingDetail.setDetailServices(addList);
            listBookingDetail.add(bookingDetail);
        }
        //set promo
        if (!ValidateUtil.isNullOrEmpty(addBookingDTO.getPromotion())) {
            Promotion promotion = promotionRepository.findById(addBookingDTO.getPromotion())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_NOT_FOUND_PROMOTION));
            LocalDate dateNow = LocalDate.now(istZoneId);
            if (dateNow.isAfter(promotion.getEndDate()) || dateNow.isBefore(promotion.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_INVALID_PROMOTION);
            }
            BigDecimal discountPrice = totalPrice.multiply(BigDecimal.valueOf(promotion.getValue()));
            discountPrice = discountPrice.setScale(0, BigDecimal.ROUND_HALF_EVEN);
            booking.setPromotion(promotion);
            totalPrice = totalPrice.subtract(discountPrice);
        }
        booking.setStartTime(dateStart.toLocalTime());
        booking.setEndTime(dateEnd.toLocalTime());
        booking.setBookingDetails(listBookingDetail);
        booking.setSlots(slotBooking);
        booking.setTotalPrice(totalPrice);
        booking.setDeposit(totalPrice);
        booking.setDistrict(addBookingDTO.getDistrict());
        booking = bookingRepository.save(booking);

        List<SitterProfile> listSitterPriority = getAvailableSitter(booking, addBookingDTO.getDistrict(), null);
        if (ValidateUtil.isNullOrEmpty(listSitterPriority)) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_NOT_FOUND_SITTER);
        }
        List<SitterProfile> listSitter = getAvailableSitter(booking, booking.getDistrict(), null);
        assigningService.assignSitter(booking, listSitter);
        return booking;
    }

}
