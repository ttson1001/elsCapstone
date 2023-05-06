package capstone.elsv2.services;

import capstone.elsv2.dto.auth.*;
import capstone.elsv2.dto.common.HealthStatusDTO;
import capstone.elsv2.dto.common.SettingDTO;
import capstone.elsv2.dto.common.SlotDTO;
import capstone.elsv2.dto.common.SlotDTOV2;
import capstone.elsv2.entities.Setting;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface CommonService {
    OTPDTO sendOTP(SendOTP sendOTP);
    Boolean checkOTP(CheckOTPDTO checkOTP);
    Boolean forgetPassword(ForgotPasswordDTO forgotPasswordDTO);
    Boolean changePassword(ChangePasswordDTO changePasswordDTO);
//    Boolean updateDeposit(float deposit);
    Boolean updateCommission(float commission);
    Boolean updateMidNight(float midNight);
    Boolean updateHoliday(float holiday);
    Boolean updateWeekend(float weekend);
    SettingDTO getDepositAndCommission();
    List<SlotDTO> getAllSlot();
    Map<Integer, SlotDTOV2> getAllSlotMap();
    boolean sendNotificationMessage(String otherId);

    List<HealthStatusDTO> getAllHealthStatus();




}
