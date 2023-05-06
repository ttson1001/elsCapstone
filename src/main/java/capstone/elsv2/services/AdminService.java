package capstone.elsv2.services;

import capstone.elsv2.dto.auth.StaffAccountDTO;
import capstone.elsv2.dto.auth.StaffDTO;
import capstone.elsv2.dto.auth.StaffDetailDTO;
import capstone.elsv2.dto.auth.UpdateStaffDTO;
import capstone.elsv2.dto.common.ChartDTO;
import capstone.elsv2.dto.common.DateRevenueDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.StatisticsDTO;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    Boolean createStaffAccount(StaffAccountDTO staffAccountDTO);

    PageDTO getAllStaff(int pageNumber, int pageSize, String keyWord);

    Boolean banStaff(String staffId);

    Boolean unBanStaff(String staffId);

    Boolean updateStaff(UpdateStaffDTO updateStaffDTO);

    StaffDetailDTO getStaffById(String id);

    DateRevenueDTO getRevenueForDate(LocalDate day);

    DateRevenueDTO getRevenueSitterForDate(LocalDate day, String sitterId);

    List<ChartDTO> getRevenueForDateInMonth(Integer month, Integer year);

    List<ChartDTO> getRevenueForDateInMonth(Integer month, Integer year, String sitterId);

    List<ChartDTO> getRevenueForAllMonthInYear(Integer year);

    List<ChartDTO> getRevenueForAllMonthInYear(Integer year, String sitterId);

    StatisticsDTO getInformationDashBoard();


}
