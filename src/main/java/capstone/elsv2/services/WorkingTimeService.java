package capstone.elsv2.services;

import capstone.elsv2.dto.workingTime.*;

import java.util.List;

public interface WorkingTimeService {
    Boolean addWorkingTimeByWeek(AddWorkingTimeWeekDTO addWorkingTimeWeekDTO);
//    Boolean addWorkingTimeByDate(AddWorkingTimeDateDTO addWorkingTimeDateDTO);

//    Boolean addWorkingTimeByMonth(AddListMonthWorkingTimeDTO addListMonthWorkingTimeDTO);
    List<WorkingTimeDTO> getAllWorkingTime(String sitterId);

    List<WorkingTimeDTO> getAllActivateWorkingTime(String sitterId);

    WorkingTimeDTO getWorkingTimeById(String workingTimeId);

    Boolean deactivateWorkingTime(String workingTimeId);

    Boolean activateWorkingTime(String workingTimeId);

//    Boolean updateWorkingTimeDateAndWeek(UpdateWorkingTimeDTO workingTimeDTO);

//    Boolean updateWorkingTimeMonth(UpdateWorkingTimeMonthDTO updateWorkingTimeMonthDTO);

//    String dayCanAdd (String sitterId);

}
