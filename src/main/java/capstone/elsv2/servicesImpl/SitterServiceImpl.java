package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.certificate.CertificateDTO;
import capstone.elsv2.dto.common.EmailDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.education.EducationDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.sitter.*;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SitterServiceImpl implements SitterService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private EducationService educationService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SitterProfileRepository sitterProfileRepository;

    @Autowired
    private SitterPackageService sitterPackageService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private SitterPackageRepository sitterPackageRepository;
    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PackageServiceRepository packageServiceRepository;

    @Override
    public PageDTO findAllSitterForAdmin(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<SitterDTO> sitterDTOS = new ArrayList<>();
        Page<SitterProfile> sitterPage = sitterProfileRepository.findAllByAccount_StatusOrAccount_Status(pageable, StatusCode.ACTIVATE.toString(), StatusCode.DEACTIVATE.toString());
        List<SitterProfile> sitters = sitterPage.getContent();
        for (SitterProfile sitter : sitters) {
            SitterDTO sitterDTO = SitterDTO.builder()
                    .id(sitter.getId())
                    .fullName(sitter.getAccount().getFullName())
                    .phone(sitter.getAccount().getPhone())
                    .email(sitter.getAccount().getEmail())
                    .address(sitter.getAccount().getSitterProfile().getAddress())
                    .gender(sitter.getAccount().getSitterProfile().getGender())
                    .status(sitter.getAccount().getStatus())
                    .build();
            sitterDTOS.add(sitterDTO);
        }
        int offset = sitterPage.getNumber() * sitterPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(sitterDTOS)
                .pageSize(sitterPage.getSize())
                .hasNextPage(sitterPage.hasNext())
                .pageIndex(sitterPage.getNumber() + 1)
                .totalRecord(sitterPage.getTotalElements())
                .toRecord(offset + sitterPage.getNumberOfElements() - 1)
                .fromRecord(offset)
                .hasPreviousPage(sitterPage.hasPrevious())
                .totalPages(sitterPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public PageDTO getAllFormSitter(String status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<SitterFormDTO> sitterDTOS = new ArrayList<>();
        Page<SitterProfile> sitterPage;
        if (status != null) {
            sitterPage = sitterProfileRepository.findAllByAccount_Status(pageable, status);
        } else {
            sitterPage = sitterProfileRepository.findAllByAccount_StatusOrAccount_Status(pageable, StatusCode.REJECTED.toString(), StatusCode.PENDING.toString());
        }
        List<SitterProfile> sitters = sitterPage.getContent();
        for (SitterProfile sitter : sitters
        ) {
            SitterFormDTO sitterDTO = SitterFormDTO.builder()
                    .id(sitter.getId())
                    .fullName(sitter.getAccount().getFullName())
                    .phone(sitter.getAccount().getPhone())
                    .email(sitter.getAccount().getEmail())
                    .address(sitter.getAddress())
                    .createDate(sitter.getAccount().getCreateDate())
                    .gender(sitter.getAccount().getSitterProfile().getGender())
                    .status(sitter.getAccount().getStatus())
                    .build();
            sitterDTOS.add(sitterDTO);
        }
        sitterDTOS.sort(Comparator.comparing(s-> s.getCreateDate()));
        Collections.reverse(sitterDTOS);
        int offset = sitterPage.getNumber() * sitterPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(sitterDTOS)
                .pageSize(sitterPage.getSize())
                .hasNextPage(sitterPage.hasNext())
                .pageIndex(sitterPage.getNumber() + 1)
                .totalRecord(sitterPage.getTotalElements())
                .toRecord(offset + sitterPage.getNumberOfElements() - 1)
                .fromRecord(offset)
                .hasPreviousPage(sitterPage.hasPrevious())
                .totalPages(sitterPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public PageDTO findAllSitter(int pageNumber, int pageSize) {
        List<SitterDTO> sitterDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<SitterProfile> sitterPage = sitterProfileRepository.findAll(pageable);
        List<SitterProfile> sitters = sitterPage.getContent();
        for (SitterProfile sitter : sitters) {
            SitterDTO sitterDTO = SitterDTO.builder()
                    .id(sitter.getId())
                    .fullName(sitter.getAccount().getFullName())
                    .phone(sitter.getAccount().getPhone())
                    .email(sitter.getAccount().getEmail())
                    .address(sitter.getAddress())
                    .gender(sitter.getGender())
                    .status(sitter.getAccount().getStatus())
                    .build();
            sitterDTOS.add(sitterDTO);
        }
        int offset = sitterPage.getNumber() * sitterPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(sitterDTOS)
                .pageSize(sitterPage.getSize())
                .hasNextPage(sitterPage.hasNext())
                .pageIndex(sitterPage.getNumber() + 1)
                .totalRecord(sitterPage.getTotalElements())
                .toRecord(offset + sitterPage.getNumberOfElements() - 1)
                .fromRecord(offset)
                .hasPreviousPage(sitterPage.hasPrevious())
                .totalPages(sitterPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public PageDTO findAllSitterByKeyWord(String keyWord, int pageNumber, int pageSize) {
        List<SitterDTO> sitterDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<SitterProfile> sitterPage = sitterProfileRepository.findAllByAccount_StatusOrAccount_Status(pageable,StatusCode.ACTIVATE.toString(), StatusCode.DEACTIVATE.toString());

        List<SitterProfile> sitters = sitterPage.getContent();
        if (keyWord.equals("")) {
            for (SitterProfile sitter : sitters) {
                SitterDTO sitterDTO = SitterDTO.builder()
                        .id(sitter.getId())
                        .fullName(sitter.getAccount().getFullName())
                        .phone(sitter.getAccount().getPhone())
                        .email(sitter.getAccount().getEmail())
                        .address(sitter.getAddress())
                        .gender(sitter.getGender())
                        .status(sitter.getAccount().getStatus())
                        .build();
                sitterDTOS.add(sitterDTO);
            }
        } else {
            for (SitterProfile sitter : sitters) {
                if (sitter.getAccount().getFullName().contains(keyWord)
                        || sitter.getAccount().getPhone().contains(keyWord)
                        || sitter.getAccount().getStatus().equals(keyWord)
                        || sitter.getAccount().getEmail().contains(keyWord)
                        || sitter.getAddress().contains(keyWord)
                        || sitter.getGender().equals(keyWord)) {
                    SitterDTO sitterDTO = SitterDTO.builder()
                            .id(sitter.getId())
                            .fullName(sitter.getAccount().getFullName())
                            .phone(sitter.getAccount().getPhone())
                            .email(sitter.getAccount().getEmail())
                            .address(sitter.getAddress())
                            .gender(sitter.getGender())
                            .status(sitter.getAccount().getStatus())
                            .build();
                    sitterDTOS.add(sitterDTO);
                }
            }
        }
        int offset = sitterPage.getNumber() * sitterPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(sitterDTOS)
                .pageSize(sitterPage.getSize())
                .hasNextPage(sitterPage.hasNext())
                .pageIndex(sitterPage.getNumber() + 1)
                .totalRecord(sitterPage.getTotalElements())
                .toRecord(offset)
                .fromRecord(offset + sitterPage.getNumberOfElements() - 1)
                .hasPreviousPage(sitterPage.hasPrevious())
                .totalPages(sitterPage.getTotalPages())
                .build();
        return pageDTO;
    }


    @Override
    public Boolean banSitter(String id) {
        Boolean isBan = false;
        Account sitter = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        if (sitter.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            sitter.setStatus(StatusCode.DEACTIVATE.toString());
            accountRepository.save(sitter);
            isBan = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không không thể bị xóa thêm lần nữa");
        }

        return isBan;
    }

    @Override
    public Boolean unBanSitter(String id) {
        Boolean isUnBan = false;
        Account sitter = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        if (sitter.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            sitter.setStatus(StatusCode.ACTIVATE.toString());
            accountRepository.save(sitter);
            isUnBan = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không không thể kích hoạt thêm lần nữa");
        }
        return isUnBan;
    }

    @Override
    public Boolean register(SitterRegisterDTO sitterRegisterDTO) {
        boolean isRegister = false;
        if (accountRepository.findByEmail(sitterRegisterDTO.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Email đã được sử dụng");
        }
        if (accountRepository.findByPhone(sitterRegisterDTO.getPhone()) != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Số tiện thoại đã được sử dụng");
        }
        try {
            Account sitter = Account.builder()
                    .email(sitterRegisterDTO.getEmail())
                    .phone(sitterRegisterDTO.getPhone())
                    .password(passwordEncoder.encode(sitterRegisterDTO.getPassword()))
                    .role(roleRepository.findAllByName("SITTER"))
                    .status(StatusCode.CREATED.toString())
                    .build();
            sitter = accountRepository.save(sitter);
            SitterProfile sitterProfile = SitterProfile.builder()
                    .account(sitter)
                    .numberOfCancels(0)
                    .rate(0f)
                    .build();
            sitterProfile = sitterProfileRepository.save(sitterProfile);

            Wallet wallet = Wallet.builder()
                    .amount(BigDecimal.valueOf(0))
                    .account(sitter)
                    .build();

            walletRepository.save(wallet);


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

            isRegister = true;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "không thể tạo được chăm sóc viên");
        }
        return isRegister;
    }

    @Override
    public Boolean updateContact(UpdateSitterContactDTO updateSitterContactDTO) {
        boolean isUpdate = false;
        Account accountSitter = accountRepository.findById(updateSitterContactDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        try {
            accountSitter.setPhone(updateSitterContactDTO.getPhone());
            accountSitter = accountRepository.save(accountSitter);

            SitterProfile sitterProfile = sitterProfileRepository.findById(updateSitterContactDTO.getSitterId()).get();
            sitterProfile.setDescription(updateSitterContactDTO.getDescription());
            sitterProfile.setAddress(updateSitterContactDTO.getAddress());
            sitterProfile.setLongitude(updateSitterContactDTO.getLongitude());
            sitterProfile.setLatitude(updateSitterContactDTO.getLatitude());
            sitterProfile.setAccount(accountSitter);
            Zone zone = zoneRepository.findByDistrict(updateSitterContactDTO.getDistrict());
            sitterProfile.setZone(zone);
            sitterProfileRepository.save(sitterProfile);
            isUpdate = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(400), "không chỉnh sửa thông tin liên hệ của chăm sóc viên");
        }
        return isUpdate;
    }

    @Override
    public SitterContactDTO getContact(String sitterId) {
        Account sitter = accountRepository.findById(sitterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        String address = "";
        String zone = "";
        double latitude = 0;
        double longitude = 0;
        if (sitter.getSitterProfile().getAddress() != null) {
            address = sitter.getSitterProfile().getAddress();
            zone = sitter.getSitterProfile().getZone().getDistrict();
            latitude = sitter.getSitterProfile().getLatitude();
            longitude = sitter.getSitterProfile().getLongitude();
        }
        SitterContactDTO sitterContactDTO = SitterContactDTO.builder()
                .sitterId(sitter.getId())
                .address(address)
                .email(sitter.getEmail())
                .phone(sitter.getPhone())
                .longitude(sitter.getSitterProfile().getLongitude())
                .latitude(sitter.getSitterProfile().getLatitude())
                .district(zone)
                .description(sitter.getSitterProfile().getDescription())
                .build();
        return sitterContactDTO;
    }

    @Override
    public Boolean updateInformation(SitterInformationDTO sitterInformationDTO) {
        boolean isUpdate = false;
        Account accountSitter = accountRepository.findById(sitterInformationDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        try {
            accountSitter.setFullName(sitterInformationDTO.getFullName());
            accountSitter = accountRepository.save(accountSitter);

            SitterProfile sitterProfile = sitterProfileRepository.findById(sitterInformationDTO.getSitterId()).get();
            sitterProfile.setDob(sitterInformationDTO.getDob());
            sitterProfile.setGender(sitterInformationDTO.getGender());
            sitterProfile.setIdCardNumber(sitterInformationDTO.getIdNumber());
            sitterProfile.setAvatarImgUrl(sitterInformationDTO.getAvatarImg());
            sitterProfile.setFrontCardImgUrl(sitterInformationDTO.getFrontCardImg());
            sitterProfile.setBackCardImgUrl(sitterInformationDTO.getBackCardImg());
            sitterProfile.setAccount(accountSitter);
            sitterProfileRepository.save(sitterProfile);
            isUpdate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể cập nhật thông tin cá nhân");
        }
        return isUpdate;
    }

    @Override
    public SitterInformationDTO getInformation(String sitterId) {
        Account sitter = accountRepository.findById(sitterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        SitterInformationDTO sitterInformationDTO = SitterInformationDTO.builder()
                .sitterId(sitter.getId())
                .fullName(sitter.getFullName())
                .dob(sitter.getSitterProfile().getDob())
                .gender(sitter.getSitterProfile().getGender())//
                .idNumber(sitter.getSitterProfile().getIdCardNumber())
                .phone(sitter.getPhone())
                .avatarImg(sitter.getSitterProfile().getAvatarImgUrl())
                .frontCardImg(sitter.getSitterProfile().getFrontCardImgUrl())
                .backCardImg(sitter.getSitterProfile().getBackCardImgUrl())
                .status(sitter.getStatus())
                .build();
        return sitterInformationDTO;
    }

    @Override
    public SitterDetailDTO findSitterById(String id) {
        SitterProfile sitter = sitterProfileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        List<CertificateDTO> certificateDTOList = certificateService.getAllCertificateBySitterId(id);
        List<EducationDTO> educationDTOS = educationService.findAllBySitterId(id);
        List<PackageDTO> packageDTOS = sitterPackageService.findAllPackageBySitterId(id);
        SitterDetailDTO sitterDetailDTO = SitterDetailDTO.builder()
                .id(sitter.getId())
                .email(sitter.getAccount().getEmail())
                .fullName(sitter.getAccount().getFullName())
                .phone(sitter.getAccount().getPhone())
                .gender(sitter.getGender())
                .address(sitter.getAddress())
                .dob(sitter.getDob())
                .avatarImgUrl(sitter.getAvatarImgUrl())
                .backCardImgUrl(sitter.getBackCardImgUrl())
                .frontCardImgUrl(sitter.getFrontCardImgUrl())
                .createDate(sitter.getAccount().getCreateDate())
                .tokenDevice(sitter.getAccount().getDeviceId())
                .description(sitter.getAccount().getSitterProfile().getDescription())
                .idCardNumber(sitter.getAccount().getSitterProfile().getIdCardNumber())
                .packageDTOS(packageDTOS)
                .reason(sitter.getReason())
                .status(sitter.getAccount().getStatus())
                .certificateDTOList(certificateDTOList)
                .educationDTOS(educationDTOS)
                .build();
        return sitterDetailDTO;

    }

    @Override
    public Boolean acceptSitter(String id) {
        boolean isAccept = false;
        Account sitter = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        if (sitter.getStatus().equals(StatusCode.PENDING.toString())) {
            sitter.setStatus(StatusCode.ACTIVATE.toString());
            accountRepository.save(sitter);
            notificationService.sendNotification(sitter.getId(), "Thông báo", "Chúc mừng bạn đã trờ thành thành viên của ELS");
            isAccept = true;

            List<SitterPackage> sitterPackages = sitterPackageRepository.findAllBySitterProfile_Id(sitter.getId());
            for (SitterPackage sitterPackage : sitterPackages) {
                sitterPackage.setStatus(StatusCode.ACTIVATE.toString());
                sitterPackageRepository.save(sitterPackage);
            }


            EmailDTO emailDetails = EmailDTO.builder()
                    .email(sitter.getEmail())
                    .subject("Thông báo đăng ký thành công tài khoản của ELS")
                    .massage("Xin chào " + sitter.getFullName() + ",\n" +
                            "\n" +
                            "Chúng tôi xin chúc mừng bạn đã trở thành nhân viên chăm sóc của Elderly Sitter. Sau khi đọc bản đăng ký của bạn, chúng tôi rất ấn tượng về kĩ năng chuyên môn cũng như kinh nghiệm làm việc của anh/chị. \n" +
                            "\n" +
                            "Trân trọng,\n" +
                            "\n" +
                            "Phòng Quản lý Nhân sự.\n" +
                            "(Đây là email được gửi tự động, Quý khách vui lòng không hồi đáp theo địa chỉ email này.)")
                    .build();
            emailService.sendSimpleMail(emailDetails);

        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Sitter đã được chấp thuận rồi không thể chấp thuận nữa");
        }
        return isAccept;
    }

    @Override
    public Boolean rejectSitter(RejectSitterDTO rejectSitterDTO) {
        boolean isRejectSitter = false;
        Account sitter = accountRepository.findById(rejectSitterDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        if (sitter.getStatus().equals(StatusCode.PENDING.toString())) {
            sitter.setStatus(StatusCode.REJECTED.toString());
            accountRepository.save(sitter);
            List<SitterPackage> sitterPackages = sitterPackageRepository.findAllBySitterProfile_Id(sitter.getId());
            sitterPackageRepository.deleteAll(sitterPackages);
            SitterProfile sitterProfile = sitterProfileRepository.findById(sitter.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
            sitterProfile.setReason(rejectSitterDTO.getReason());
            sitterProfileRepository.save(sitterProfile);
            sitterProfileRepository.save(sitterProfile);
            notificationService.sendNotification(sitter.getId(), "Thông báo", rejectSitterDTO.getReason());
            EmailDTO emailDetails = EmailDTO.builder()
                    .email(sitter.getEmail())
                    .subject("Thông báo về kết quả đăng ký tài khoản ELS")
                    .massage("Xin chào " + sitter.getFullName() + ",\n" +
                            "\n" +
                            "Chúng tôi đã đọc và rất ấn tượng với kĩ năng chuyên môn và kinh nghiệm làm việc mà bạn đã ghi trong form đăng ký. \n" +
                            "Nhưng chúng tôi rất tiếc khi phải thông báo rằng bạn không phù hợp với tiêu chí và mục tiêu của ELS.\n" +
                            "\n" +
                            "Mong sớm được hợp tác cùng bạn trong tương lai.\n" +
                            "\n" +
                            "Trân trọng,\n" +
                            "\n" +
                            "Phòng Quản lý Nhân sự.\n" +
                            "(Đây là email được gửi tự động, Quý khách vui lòng không hồi đáp theo địa chỉ email này.)")
                    .build();
            emailService.sendSimpleMail(emailDetails);

            isRejectSitter = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Sitter đã được từ chối không thể từ chối một lần nữa ");
        }
        return isRejectSitter;
    }

    @Override
    public Boolean sendForm(String id) {
        boolean isSend = false;
        Account sitter = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        if (sitter.getStatus().equals(StatusCode.CREATED.toString()) || sitter.getStatus().equals(StatusCode.REJECTED.toString())) {
            sitter.setStatus(StatusCode.PENDING.toString());
            sitter.setCreateDate(LocalDate.now());
            accountRepository.save(sitter);
            isSend = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đơn không gửi được vui lòng kiểm tra lại ");
        }
        return isSend;
    }

    @Override
    public PageDTO findAllSitterReject(int pageNumber, int pageSize) {
        List<SitterDTO> sitterDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<SitterProfile> sitterPage = sitterProfileRepository.findAllByAccount_StatusOrAccount_Status(pageable, StatusCode.REJECTED.toString(), StatusCode.PENDING.toString());
        List<SitterProfile> sitters = sitterPage.getContent();
        for (SitterProfile sitter : sitters) {
            SitterDTO sitterDTO = SitterDTO.builder()
                    .id(sitter.getId())
                    .fullName(sitter.getAccount().getFullName())
                    .phone(sitter.getAccount().getPhone())
                    .email(sitter.getAccount().getEmail())
                    .reason(sitter.getReason())
                    .address(sitter.getAddress())
                    .gender(sitter.getGender())
                    .status(sitter.getAccount().getStatus())
                    .build();
            sitterDTOS.add(sitterDTO);
        }
        int offset = sitterPage.getNumber() * sitterPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(sitterDTOS)
                .pageSize(sitterPage.getSize())
                .hasNextPage(sitterPage.hasNext())
                .pageIndex(sitterPage.getNumber() + 1)
                .totalRecord(sitterPage.getTotalElements())
                .fromRecord(offset)
                .toRecord((offset + sitterPage.getNumberOfElements() - 1))
                .hasPreviousPage(sitterPage.hasPrevious())
                .totalPages(sitterPage.getTotalPages())
                .build();
        return pageDTO;
    }

//    @Override
//    public StatisticsDTO countTotal() {
//        StatisticsDTO statisticsDTO = new StatisticsDTO();
//        statisticsDTO.setTotal(sitterProfileRepository.findAllByAccount_StatusOrAccount_Status(StatusCode.ACTIVATE.toString(), StatusCode.DEACTIVATE.toString()).size());
//        return statisticsDTO;
//    }

//    @Override
//    public Set<OrderChatDTO> getAllMessageBySitterId(String sitterId) {
//        List<Booking> bookings = bookingRepository.findAllBySitter_Id(sitterId);
//        Set<OrderChatDTO> orderChatDTOS = new HashSet<>();
//        for (Booking booking : bookings) {
//            if (StatusCode.ACTIVATE.toString().equals(booking.getStatus()) || StatusCode.COMPLETED.toString().equals(booking.getStatus())) {
//                OrderChatDTO orderChatDTO = OrderChatDTO.builder()
//                        .otherId(booking.getCustomer().getId())
//                        .otherAvatar(booking.getCustomer().getAvatarImgUrl())
//                        .otherEmail(booking.getCustomer().getAccount().getEmail())
//                        .otherName(booking.getCustomer().getAccount().getFullName())
//                        .build();
//                orderChatDTOS.add(orderChatDTO);
//            }
//        }
//        return orderChatDTOS;
//    }
}
