package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.auth.StaffAccountDTO;
import capstone.elsv2.dto.auth.StaffDTO;
import capstone.elsv2.dto.auth.StaffDetailDTO;
import capstone.elsv2.dto.auth.UpdateStaffDTO;
import capstone.elsv2.dto.common.ChartDTO;
import capstone.elsv2.dto.common.DateRevenueDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.StatisticsDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.emunCode.TypeCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.StaffProfile;
import capstone.elsv2.entities.Transaction;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final String ERROR_NOT_FOUND_STAFF = "Không tìm thấy nhân viên nào";
    private final String ERROR_UPDATE_STAFF = "Không thể chỉnh sửa thông tin nhân viên";
    private final String ERROR_CREATE_STAFF = "Không thể tạo nhân viên này được";
    private final String ERROR_BAN = "Nhân viên đã bị khóa không thể khóa nhân viên thêm một lần nữa";
    private final String ERROR_UNBAN = "Nhân viên đã được kích hoạt không thể kích hoạt nhân viên thêm một lần nữa";
    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final BookingRepository bookingRepository;
    private final ElderRepository elderRepository;

    public AdminServiceImpl(RoleRepository roleRepository,
                            AccountRepository accountRepository,
                            StaffProfileRepository staffProfileRepository,
                            WalletTransactionRepository walletTransactionRepository,
                            BookingRepository bookingRepository,
                            ElderRepository elderRepository) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.bookingRepository = bookingRepository;
        this.elderRepository = elderRepository;
    }

    @Override
    public Boolean createStaffAccount(StaffAccountDTO staffAccountDTO) {
        try {

            Account account = Account.builder()
                    .email(staffAccountDTO.getEmail())
                    .fullName(staffAccountDTO.getFullName())
                    .role(roleRepository.findAllByName("STAFF"))
                    .phone(staffAccountDTO.getPhone())
                    .createDate(LocalDate.now())
                    .status(StatusCode.ACTIVATE.toString())
                    .password(passwordEncoder.encode(staffAccountDTO.getPassword()))
                    .build();
            accountRepository.save(account);
            account = accountRepository.findByEmail(account.getEmail());
            StaffProfile staffProfile = StaffProfile.builder()
                    .dob(staffAccountDTO.getDob())
                    .account(account)
                    .address(staffAccountDTO.getAddress())
                    .avatarImg(staffAccountDTO.getAvatarImage())
                    .gender(staffAccountDTO.getGender())
                    .build();
            staffProfileRepository.save(staffProfile);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_CREATE_STAFF);
        }
        return true;
    }


    @Override
    public PageDTO getAllStaff(int pageNumber, int pageSize, String keyWord) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<StaffDTO> staffDTOS = new ArrayList<>();
        Page<StaffProfile> staffPage;
        staffPage = staffProfileRepository.findAll(pageable);
        List<StaffProfile> staffs = staffPage.getContent();
        System.out.println(keyWord.equals(""));
        if (!keyWord.equals("")) {
            for (StaffProfile staff : staffs) {
                if (staff.getAccount().getStatus().equals(keyWord) || staff.getAccount().getEmail().contains(keyWord) || staff.getAccount().getFullName().contains(keyWord)) {
                    StaffDTO staffDTO = StaffDTO.builder()
                            .id(staff.getId())
                            .email(staff.getAccount().getEmail())
                            .fullName(staff.getAccount().getFullName())
                            .status(staff.getAccount().getStatus())
                            .build();
                    staffDTOS.add(staffDTO);
                }
            }
        } else {
            for (StaffProfile staff : staffs) {
                StaffDTO staffDTO = StaffDTO.builder()
                        .id(staff.getId())
                        .email(staff.getAccount().getEmail())
                        .fullName(staff.getAccount().getFullName())
                        .status(staff.getAccount().getStatus())
                        .build();
                staffDTOS.add(staffDTO);
            }
        }
        PageDTO pageDTO = PageDTO.builder()
                .data(staffDTOS)
                .pageSize(staffPage.getSize())
                .hasNextPage(staffPage.hasNext())
                .pageIndex(staffPage.getNumber() + 1)
                .hasPreviousPage(staffPage.hasPrevious())
                .totalPages(staffPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public Boolean banStaff(String staffId) {
        Account staff = accountRepository.findById(staffId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND_STAFF));
        if (staff.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            staff.setStatus(StatusCode.DEACTIVATE.toString());
            accountRepository.save(staff);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), ERROR_BAN);
        }
        return true;
    }

    @Override
    public Boolean unBanStaff(String staffId) {
        Account staff = accountRepository.findById(staffId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND_STAFF));
        if (staff.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            staff.setStatus(StatusCode.ACTIVATE.toString());
            accountRepository.save(staff);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), ERROR_UNBAN);
        }
        return true;
    }

    @Override
    public Boolean updateStaff(UpdateStaffDTO updateStaffDTO) {
        try {
            Account staff = accountRepository.findById(updateStaffDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND_STAFF));
            staff.setFullName(updateStaffDTO.getFullName());
            staff.setPhone(updateStaffDTO.getPhone());
            accountRepository.save(staff);
            StaffProfile staffProfile = staffProfileRepository.findById(updateStaffDTO.getId()).get();
            staffProfile.setDob(updateStaffDTO.getDob());
            staffProfile.setAddress(updateStaffDTO.getAddress());
            staffProfile.setGender(updateStaffDTO.getGender());
            staffProfile.setAvatarImg(updateStaffDTO.getAvatarImage());
            staffProfileRepository.save(staffProfile);
            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_UPDATE_STAFF);
        }

    }

    @Override
    public StaffDetailDTO getStaffById(String id) {
        Account staff = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND_STAFF));
        StaffProfile staffProfile = staff.getStaffProfile();

        StaffDetailDTO staffDetailDTO = StaffDetailDTO.builder()
                .id(staff.getId())
                .email(staff.getEmail())
                .fullName(staff.getFullName())
                .phone(staff.getPhone())
                .address(staffProfile.getAddress())
                .gender(staffProfile.getGender())
                .avatarImage(staffProfile.getAvatarImg())
                .dob(staffProfile.getDob())
                .build();
        return staffDetailDTO;
    }

    @Override
    public DateRevenueDTO getRevenueForDate(LocalDate day) {
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        DateRevenueDTO dateRevenueDTO = DateRevenueDTO.builder()
                .date(day)
                .amount(totalRevenue)
                .build();

        List<Transaction> walletTransactions = walletTransactionRepository.findAllByDate(day.getDayOfMonth(), day.getMonthValue(), day.getYear(), TypeCode.REVENUE.toString());
        if (walletTransactions.size() != 0) {
            for (Transaction walletTransaction : walletTransactions) {
                if (walletTransaction.getType().equals(TypeCode.REVENUE.toString())) {
                    totalRevenue = totalRevenue.add(walletTransaction.getAmount());
                }
            }

            dateRevenueDTO.setDate(day);
            dateRevenueDTO.setAmount(totalRevenue);

        }
        return dateRevenueDTO;
    }

    @Override
    public DateRevenueDTO getRevenueSitterForDate(LocalDate day, String sitterId) {
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        DateRevenueDTO dateRevenueDTO = DateRevenueDTO.builder()
                .date(day)
                .amount(totalRevenue)
                .build();

        List<Transaction> walletTransactions = walletTransactionRepository.findAllByDateForSitter(day.getDayOfMonth(), day.getMonthValue(), day.getYear(), TypeCode.RECEIVE.toString(), sitterId);
        if (walletTransactions.size() != 0) {
            for (Transaction walletTransaction : walletTransactions) {
                if (walletTransaction.getType().equals(TypeCode.RECEIVE.toString())) {
                    totalRevenue = totalRevenue.add(walletTransaction.getAmount());
                }
            }

            dateRevenueDTO.setDate(day);
            dateRevenueDTO.setAmount(totalRevenue);

        }
        return dateRevenueDTO;
    }

    @Override
    public List<ChartDTO> getRevenueForDateInMonth(Integer month, Integer year) {
        List<ChartDTO> chartDTOS = new ArrayList<>();
        Map<Integer, BigDecimal> integerBigDecimalMap = new HashMap<>();
        List<Transaction> walletTransactions = walletTransactionRepository.findAllByMonth(month, year, TypeCode.REVENUE.toString());
        LocalDate localDate = LocalDate.of(year, month, 1);
        Integer integer = localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        for (int i = 1; i <= integer; i++) {
            integerBigDecimalMap.put(i, BigDecimal.valueOf(0));
        }
        for (Transaction walletTransaction : walletTransactions) {
            if (integerBigDecimalMap.containsKey(walletTransaction.getCreateDateTime().getDayOfMonth()))
                integerBigDecimalMap.put(walletTransaction.getCreateDateTime().getDayOfMonth(), integerBigDecimalMap.get(walletTransaction.getCreateDateTime().getDayOfMonth()).add(walletTransaction.getAmount()));
        }
        Set set = integerBigDecimalMap.keySet();
        for (Object key : set) {
            ChartDTO chartDTO = ChartDTO.builder()
                    .value(Integer.parseInt(key + ""))
                    .amount(integerBigDecimalMap.get(key))
                    .build();
            chartDTOS.add(chartDTO);
        }

        return chartDTOS;


    }

    @Override
    public List<ChartDTO> getRevenueForDateInMonth(Integer month, Integer year, String sitterId) {
        List<ChartDTO> chartDTOS = new ArrayList<>();
        Map<Integer, BigDecimal> integerBigDecimalMap = new HashMap<>();
        List<Transaction> walletTransactions = walletTransactionRepository.findAllByMonthForSitter(month, year, TypeCode.RECEIVE.toString(), sitterId);
        LocalDate localDate = LocalDate.of(year, month, 1);
        Integer integer = localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        for (int i = 1; i <= integer; i++) {
            integerBigDecimalMap.put(i, BigDecimal.valueOf(0));
        }
        for (Transaction walletTransaction : walletTransactions) {
            if (integerBigDecimalMap.containsKey(walletTransaction.getCreateDateTime().getDayOfMonth()))
                integerBigDecimalMap.put(walletTransaction.getCreateDateTime().getDayOfMonth(), integerBigDecimalMap.get(walletTransaction.getCreateDateTime().getDayOfMonth()).add(walletTransaction.getAmount()));
        }
        Set set = integerBigDecimalMap.keySet();
        for (Object key : set) {
            ChartDTO chartDTO = ChartDTO.builder()
                    .value(Integer.parseInt(key + ""))
                    .amount(integerBigDecimalMap.get(key))
                    .build();
            chartDTOS.add(chartDTO);
        }

        return chartDTOS;
    }

    @Override
    public List<ChartDTO> getRevenueForAllMonthInYear(Integer year) {
        List<ChartDTO> chartDTOS = new ArrayList<>();
        chartDTOS.add(new ChartDTO(1, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(2, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(3, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(4, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(5, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(6, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(7, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(8, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(9, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(10, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(11, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(12, BigDecimal.valueOf(0)));

        List<Transaction> walletTransactions = walletTransactionRepository.findAllByYear(year, TypeCode.REVENUE.toString());
        for (Transaction walletTransaction : walletTransactions) {
            for (ChartDTO chartDTO : chartDTOS) {
                if (walletTransaction.getCreateDateTime().getMonthValue() == chartDTO.getValue()) {
                    chartDTO.setAmount(chartDTO.getAmount().add(walletTransaction.getAmount()));
                }
            }
        }
        return chartDTOS;
    }

    @Override
    public List<ChartDTO> getRevenueForAllMonthInYear(Integer year, String sitterId) {
        List<ChartDTO> chartDTOS = new ArrayList<>();
        chartDTOS.add(new ChartDTO(1, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(2, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(3, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(4, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(5, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(6, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(7, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(8, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(9, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(10, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(11, BigDecimal.valueOf(0)));
        chartDTOS.add(new ChartDTO(12, BigDecimal.valueOf(0)));

        List<Transaction> walletTransactions = walletTransactionRepository.findAllByYearForSitter(year, TypeCode.RECEIVE.toString(), sitterId);
        for (Transaction walletTransaction : walletTransactions) {
            for (ChartDTO chartDTO : chartDTOS) {
                if (walletTransaction.getCreateDateTime().getMonthValue() == chartDTO.getValue()) {
                    chartDTO.setAmount(chartDTO.getAmount().add(walletTransaction.getAmount()));
                }
            }
        }
        return chartDTOS;
    }

    @Override
    public StatisticsDTO getInformationDashBoard() {
        StatisticsDTO statisticsDTO = StatisticsDTO.builder()
                .totalBooking(bookingRepository.countAllByStatusOrStatusOrStatusOrStatus(StatusCode.ACTIVATE.toString(), StatusCode.COMPLETED.toString(), StatusCode.PAID.toString(), StatusCode.PENDING.toString()))
                .totalCustomer(accountRepository.countAccountByRole_Name("CUSTOMER"))
                .totalElder(elderRepository.countAllByStatus(StatusCode.ACTIVATE.toString()))
                .totalSitter(accountRepository.countAccountByRole_Name("SITTER"))
                .build();

        return statisticsDTO;
    }
}
