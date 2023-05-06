package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.auth.*;
import capstone.elsv2.dto.common.HealthStatusDTO;
import capstone.elsv2.dto.common.SettingDTO;
import capstone.elsv2.dto.common.SlotDTO;
import capstone.elsv2.dto.common.SlotDTOV2;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.Setting;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.RoleRepository;
import capstone.elsv2.repositories.SettingRepository;
import capstone.elsv2.services.CommonService;
import capstone.elsv2.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public OTPDTO sendOTP(SendOTP sendOTP) {
        OTPDTO otpdto = new OTPDTO();
        Account account = accountRepository.findByPhone(sendOTP.getPhone());
        if (account == null)
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Số điện thoại chưa được đăng ký");
        try {
            // làm hàm gư otp đến số điện thoại
            String otp = "0000";
            account.setOtp(otp);
            account.setDeviceId(sendOTP.getDeviceId());
            account = accountRepository.save(account);
            otpdto.setOtp(otp);
            notificationService.sendNotification(account.getId(), "Mã OTP của bạn", "0000");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể gửi mã otp");
        }
        return otpdto;
    }

    @Override
    public Boolean checkOTP(CheckOTPDTO checkOTPDTO) {
        boolean isCheck;
        Account user = accountRepository.findByPhone(checkOTPDTO.getPhone());
        if (user.getOtp().equals(checkOTPDTO.getOtp())) {
            user.setOtp("");
            accountRepository.save(user);
            isCheck = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Mã otp không đúng vui lòng nhập lại");
        }
        return isCheck;
    }

    @Override
    public Boolean forgetPassword(ForgotPasswordDTO forgotPasswordDTO) {
        Boolean isUpdate = false;
        try {
            Account user = accountRepository.findByPhone(forgotPasswordDTO.getPhone());
            user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
            accountRepository.save(user);
            isUpdate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi mật khẩu được");
        }
        return isUpdate;
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        Boolean isUpdate = false;
        try {
            Account user = accountRepository.findByEmail(changePasswordDTO.getEmail());
            Boolean check = passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword());
            if (check == true) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                accountRepository.save(user);
            } else {
                throw new Exception();
            }
            isUpdate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi mật khẩu được");
        }
        return isUpdate;
    }

//    @Override
//    @Transactional
//    public Boolean updateDeposit(float deposit) {
//        try {
//            Setting setting = settingRepository.findById("1").get();
//            setting.setDeposit(deposit);
//            settingRepository.save(setting);
//            return true;
//        } catch (Exception é) {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi phần trăm tiền đặt cọc");
//        }
//
//    }

    @Override
    @Transactional
    public Boolean updateCommission(float commission) {
        try {
            Setting setting = settingRepository.findById("1").get();
            setting.setCommission(new BigDecimal(commission).setScale(1, RoundingMode.HALF_UP).floatValue());
            settingRepository.save(setting);
            return true;
        } catch (Exception é) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi chiết khấu");
        }
    }

    @Override
    public Boolean updateMidNight(float midNight) {
        try {
            Setting setting = settingRepository.findById("1").get();
            setting.setMidnight(new BigDecimal(midNight).setScale(1, RoundingMode.HALF_UP).floatValue());
            settingRepository.save(setting);
            return true;
        } catch (Exception é) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi chiết khấu");
        }
    }

    @Override
    public Boolean updateHoliday(float holiday) {
        try {
            Setting setting = settingRepository.findById("1").get();
            setting.setHoliday(new BigDecimal(holiday).setScale(1, RoundingMode.HALF_UP).floatValue());
            settingRepository.save(setting);
            return true;
        } catch (Exception é) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi chiết khấu");
        }
    }

    @Override
    public Boolean updateWeekend(float weekend) {
        try {
            Setting setting = settingRepository.findById("1").get();
            setting.setWeekend(new BigDecimal(weekend).setScale(1, RoundingMode.HALF_UP).floatValue());
            settingRepository.save(setting);
            return true;
        } catch (Exception é) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể đổi chiết khấu");
        }
    }

    @Override
    public SettingDTO getDepositAndCommission() {
        Setting setting = settingRepository.findById("1").get();
        SettingDTO settingDTO = SettingDTO.builder()
                .deposit(new BigDecimal(setting.getDeposit()).setScale(1, RoundingMode.HALF_UP).floatValue())
                .midnight(new BigDecimal(setting.getMidnight()).setScale(1, RoundingMode.HALF_UP).floatValue())
                .weekend(new BigDecimal(setting.getWeekend()).setScale(1, RoundingMode.HALF_UP).floatValue())
                .holiday(new BigDecimal(setting.getHoliday()).setScale(1, RoundingMode.HALF_UP).floatValue())
                .commission(setting.getCommission())
                .build();
        return settingDTO;
    }

    @Override
    public boolean sendNotificationMessage(String otherId) {
        Account account = accountRepository.findById(otherId).get();
        if (account.getRole().getName().equals("SITTER")) {
            notificationService.sendNotification(otherId, "Thông báo", "Bạn có một tin nhắn mới từ chăm sóc viên" + account.getFullName());
        }else {
            notificationService.sendNotification(otherId, "Thông báo", "Bạn có một tin nhắn mới từ khách hàng" + account.getFullName());
        } return true;
    }

    @Override
    public List<SlotDTO> getAllSlot() {
        List<SlotDTO> slotDTOS = new ArrayList<>();
        List<String> slots = new ArrayList<>();
        slots.add("1 00:00-02:00");
        slots.add("2 02:00-04:00");
        slots.add("3 04:00-06:00");
        slots.add("4 06:00-08:00");
        slots.add("5 08:00-10:00");
        slots.add("6 10:00-12:00");
        slots.add("7 12:00-14:00");
        slots.add("8 14:00-16:00");
        slots.add("9 16:00-18:00");
        slots.add("10 18:00-20:00");
        slots.add("11 20:00-22:00");
        slots.add("12 22:00-24:00");

        for (String slot : slots) {
            SlotDTO slotDTO = SlotDTO.builder()
                    .slots(slot)
                    .build();
            slotDTOS.add(slotDTO);
        }
        return slotDTOS;
    }

    @Override
    public Map<Integer, SlotDTOV2> getAllSlotMap() {
        return  IntStream.rangeClosed(0, 11)
                .boxed()
                .collect(Collectors.toMap(
                        i -> i+1,
                        i -> new SlotDTOV2(LocalTime.of(i * 2, 0), LocalTime.of(i * 2 + 2 == 24 ? 23 : i * 2 + 2, i * 2 + 2 == 24 ? 59 : 0))
                ));
    }

    @Override
    public List<HealthStatusDTO> getAllHealthStatus() {
        List<HealthStatusDTO> healthStatusDTOList = new ArrayList<>();
        healthStatusDTOList.add(new HealthStatusDTO("Người cao tuổi cần sự hỗ trợ sinh hoạt"));
        healthStatusDTOList.add(new HealthStatusDTO("Người cao tuổi không có khả năng sinh hoạt"));
        healthStatusDTOList.add(new HealthStatusDTO("Người cao tuổi tự đi đứng sinh hoạt"));

        return healthStatusDTOList;
    }
}
