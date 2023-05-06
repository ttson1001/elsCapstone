package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.workingTime.*;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.entities.WorkingTime;
import capstone.elsv2.mapper.WorkingTimeMapper;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.repositories.WorkingTimeRepository;
import capstone.elsv2.services.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkingTimeServiceImpl implements WorkingTimeService {

    private final String ERROR_ADD_WORKING_TIME = "Không tạo được ngày làm việc cho chăm sóc viên";
    private final String ERROR_ADD_WORKING_TIME_SLOTS = " Có giờ làm việc không phù hợp là giờ ";
    private final String ERROR_NOT_FOUND = "Không tìm thấy thời gian làm việc của chăm sóc viên";
    private final String ERROR_FORMAT = "Vui lòng gửi theo đúng mẫu Ví dụ: MONDAY 1-2-3;TUESDAY 3-4-5";
    private final String ERROR_UPDATE = "Không thể chỉnh sửa ngày làm viêc của bạn";
    private final String ERROR_SIZE = "Không thể thêm ngày làm việc nữa vì đã đủ bảy ngày làm việc vui lòng vào chỉnh sửa để chỉnh sửa lại giờ làm việc";
    private final String ERROR_ADD_DATE = "Ngày này đã có rồi vui lòng không thêm nữa";
    @Autowired
    WorkingTimeRepository workingTimeRepository;
    @Autowired
    private SitterProfileRepository sitterProfileRepository;

    @Override
    @Transactional
    // add theo ngày
    public Boolean addWorkingTimeByWeek(AddWorkingTimeWeekDTO addWorkingTimeDTO) {
        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(addWorkingTimeDTO.getSitterId());
//
//        SitterProfile sitter = sitterProfileRepository.findById(addWorkingTimeDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        String dayAndSlots[] = addWorkingTimeDTO.getDayOfWeekAndSlots().split(";");
        for (String dayAndSlot : dayAndSlots) {
            int check = checkErrorSlot(dayAndSlot.split(" ")[1]);
            if (check != -1)
                throw new ResponseStatusException(HttpStatus.valueOf(400), dayAndSlot.split(" ")[0] + ERROR_ADD_WORKING_TIME_SLOTS + dayAndSlot.split(" ")[1]);
        }


        try {
            // truong hop co roi
            for (String dayAndSlot : dayAndSlots) {
                for (WorkingTime workingTime : workingTimes) {
                    if (workingTime.getDayOfWeek().equals(dayAndSlot.split(" ")[0])) {
                        workingTime.setSlots(dayAndSlot.split(" ")[1]);
                        workingTime.setStatus(StatusCode.ACTIVATE.toString());
                        workingTimeRepository.save(workingTime);
                    }
                }

            }

            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), ERROR_ADD_WORKING_TIME);
        }
    }


//    @Override
//    public Boolean addWorkingTimeByDate(AddWorkingTimeDateDTO addWorkingTimeDateDTO) {
//        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(addWorkingTimeDateDTO.getSitterId());
//        for (WorkingTime workingTime : workingTimes) {
//            workingTime.setIsMonth(false);
//            workingTime.setIsWeek(false);
//            workingTimeRepository.save(workingTime);
//        }
//
//        if (checkErrorSlot(addWorkingTimeDateDTO.getSlots()) != -1)
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + addWorkingTimeDateDTO.getSlots());
//        for (WorkingTime workingTime : workingTimes) {
//            workingTime.setSlots(addWorkingTimeDateDTO.getSlots());
//            workingTime.setIsMonth(false);
//            workingTime.setIsDate(true);
//            workingTime.setIsWeek(false);
//            workingTimeRepository.save(workingTime);
//        }
//
//        return true;
//    }

//    @Transactional
//    public Boolean addWorkingTimeByMonth(AddListMonthWorkingTimeDTO addListMonthWorkingTimeDTO) {
//        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(addListMonthWorkingTimeDTO.getSitterId());
//        for (WorkingTime workingTime : workingTimes) {
//            workingTime.setIsDate(false);
//            workingTime.setIsWeek(false);
//            workingTimeRepository.save(workingTime);
//        }
//
//        List<AddMonthWorkingTimeDTO> addMonthWorkingTimeDTOS = addListMonthWorkingTimeDTO.getAddMonthWorkingTimeDTOS();
//        for (WorkingTime workingTime : workingTimes) {
//            for (int i = 0; i < addMonthWorkingTimeDTOS.size(); i++) {
//                if (checkErrorSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekOne()) != -1)
//                    throw new ResponseStatusException(HttpStatus.valueOf(400), addMonthWorkingTimeDTOS.get(i).getDayOfWeek() + ERROR_ADD_WORKING_TIME_SLOTS + addMonthWorkingTimeDTOS.get(i).getSlotWeekOne());
//                if (checkErrorSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekTwo()) != -1)
//                    throw new ResponseStatusException(HttpStatus.valueOf(400), addMonthWorkingTimeDTOS.get(i).getDayOfWeek() + ERROR_ADD_WORKING_TIME_SLOTS + addMonthWorkingTimeDTOS.get(i).getSlotWeekTwo());
//                if (checkErrorSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekThree()) != -1)
//                    throw new ResponseStatusException(HttpStatus.valueOf(400), addMonthWorkingTimeDTOS.get(i).getDayOfWeek() + ERROR_ADD_WORKING_TIME_SLOTS + addMonthWorkingTimeDTOS.get(i).getSlotWeekThree());
//                if (checkErrorSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekFour()) != -1)
//                    throw new ResponseStatusException(HttpStatus.valueOf(400), addMonthWorkingTimeDTOS.get(i).getDayOfWeek() + ERROR_ADD_WORKING_TIME_SLOTS + addMonthWorkingTimeDTOS.get(i).getSlotWeekFour());
//
//                if (workingTime.getDayOfWeek().equals(addMonthWorkingTimeDTOS.get(i).getDayOfWeek())) {
//                    workingTime.setIsMonth(true);
//                    workingTime.setWeekOneSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekOne());
//                    workingTime.setWeekTwoSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekTwo());
//                    workingTime.setWeekThreeSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekThree());
//                    workingTime.setWeekFourSlot(addMonthWorkingTimeDTOS.get(i).getSlotWeekFour());
//                    workingTimeRepository.save(workingTime);
//                    addMonthWorkingTimeDTOS.remove(i);
//                }
//            }
//        }
//
//        return true;
//
//
//    }

    @Override
    public List<WorkingTimeDTO> getAllWorkingTime(String sitterId) {
        List<WorkingTimeDTO> workingTimeDTOS = new ArrayList<>();
        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(sitterId);
        for (WorkingTime workingTime : workingTimes) {
            WorkingTimeDTO workingTimeDTO = WorkingTimeDTO.builder()
                    .id(workingTime.getId())
                    .dayOfWeek(workingTime.getDayOfWeek())
                    .slots(workingTime.getSlots())
                    .status(workingTime.getStatus())
                    .build();
            workingTimeDTOS.add(workingTimeDTO);
        }
        return workingTimeDTOS;
    }

    @Override
    public List<WorkingTimeDTO> getAllActivateWorkingTime(String sitterId) {
        List<WorkingTime> workingTimeList = workingTimeRepository.findAllBySitter_IdAndAndStatus(sitterId,StatusCode.ACTIVATE.toString());
        return WorkingTimeMapper.INSTANCE.convertWorkingTimeDTOList(workingTimeList);
    }

    @Override
    public WorkingTimeDTO getWorkingTimeById(String workingTimeId) {
        WorkingTime workingTime = workingTimeRepository.findById(workingTimeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        WorkingTimeDTO workingTimeDTO = WorkingTimeDTO.builder()
                .id(workingTime.getId())
                .dayOfWeek(workingTime.getDayOfWeek())
                .slots(workingTime.getSlots())
                .status(workingTime.getStatus())
                .build();
        return workingTimeDTO;
    }

    @Override
    public Boolean deactivateWorkingTime(String workingTimeId) {
        WorkingTime workingTime = workingTimeRepository.findById(workingTimeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        if(workingTime.getStatus().equals(StatusCode.ACTIVATE.toString())){
            workingTime.setStatus(StatusCode.DEACTIVATE.toString());
            workingTimeRepository.save(workingTime);
        }else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày này đã hủy không thể hủy nữa");
        }
        return true;

    }

    @Override
    public Boolean activateWorkingTime(String workingTimeId) {
        WorkingTime workingTime = workingTimeRepository.findById(workingTimeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
        if(workingTime.getStatus().equals(StatusCode.ACTIVATE.toString())){
            workingTime.setStatus(StatusCode.DEACTIVATE.toString());
            workingTimeRepository.save(workingTime);
        }else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày này đã được kích hoạt không thể kích hoạt nữa");
        }
        return true;
    }

    //    @Override
//    public Boolean updateWorkingTimeDateAndWeek(UpdateWorkingTimeDTO updateWorkingTimeDTO) {
//        if (checkErrorSlot(updateWorkingTimeDTO.getSlots()) != -1) {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + updateWorkingTimeDTO.getSlots());
//        }
//        try {
//            WorkingTime workingTime = workingTimeRepository.findById(updateWorkingTimeDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
////        workingTime.setDayOfWeek();
//            workingTime.setSlots(updateWorkingTimeDTO.getSlots());
//            workingTimeRepository.save(workingTime);
//            return true;
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_UPDATE);
//        }
//    }

//    @Override
//    public Boolean updateWorkingTimeMonth(UpdateWorkingTimeMonthDTO updateWorkingTimeMonthDTO) {
//
//        if (checkErrorSlot(updateWorkingTimeMonthDTO.getSlotWeekOne()) != -1)
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + updateWorkingTimeMonthDTO.getSlotWeekOne());
//        if (checkErrorSlot(updateWorkingTimeMonthDTO.getSlotWeekTwo()) != -1)
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + updateWorkingTimeMonthDTO.getSlotWeekTwo());
//        if (checkErrorSlot(updateWorkingTimeMonthDTO.getSlotWeekThree()) != -1)
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + updateWorkingTimeMonthDTO.getSlotWeekThree());
//        if (checkErrorSlot(updateWorkingTimeMonthDTO.getSlotWeekFour()) != -1)
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_ADD_WORKING_TIME_SLOTS + updateWorkingTimeMonthDTO.getIsWeekFour());
//
//
//        try {
//            WorkingTime workingTime = workingTimeRepository.findById(updateWorkingTimeMonthDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), ERROR_NOT_FOUND));
//            workingTime.setWeekOneSlot(updateWorkingTimeMonthDTO.getSlotWeekOne());
//            workingTime.setWeekTwoSlot(updateWorkingTimeMonthDTO.getSlotWeekTwo());
//            workingTime.setWeekThreeSlot(updateWorkingTimeMonthDTO.getSlotWeekThree());
//            workingTime.setWeekFourSlot(updateWorkingTimeMonthDTO.getSlotWeekFour());
//            workingTimeRepository.save(workingTime);
//            return true;
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_UPDATE);
//        }
//    }

//    @Override
//    public String dayCanAdd(String sitterId) {
//        String mon = "MONDAY";
//        String tue = "TUESDAY";
//        String wed = "WEDNESDAY";
//        String thu = "THURSDAY";
//        String fri = "FRIDAY";
//        String sat = "SATURDAY";
//        String sun = "SUNDAY";
//
//        List<String> dayOfWeek = new ArrayList<>();
//        dayOfWeek.add(mon);
//        dayOfWeek.add(tue);
//        dayOfWeek.add(wed);
//        dayOfWeek.add(thu);
//        dayOfWeek.add(fri);
//        dayOfWeek.add(sat);
//        dayOfWeek.add(sun);
//        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(sitterId);
//        for (WorkingTime workingTime : workingTimes) {
//            for (int i = 0; i < dayOfWeek.size(); i++) {
//                if (workingTime.getDayOfWeek().equals(dayOfWeek.get(i))) {
//                    dayOfWeek.remove(i);
//                }
//            }
//        }
//        String dayCanAdd = "";
//        for (String day : dayOfWeek) {
//            dayCanAdd = dayCanAdd + " " + day;
//        }
//        return dayCanAdd;
//
//    }

    private int checkErrorSlot(String slot) {
//        String slot = "1-2-4-5-7-8-9-11";
        String slots[] = slot.split("-");
//        System.out.println(slots.length);


        // 1+1 = 2  -> 2 +1 != 5 i= length  tối đa phải là 4 tiếng
        // 1+1 = 2 -> 2+1 = 3
        try {
            for (int i = 0; i < slots.length; i++) {
                int n = Integer.parseInt(slots[i]);
//            System.out.println("n " + n);
                if (i == 0 && n + 1 != Integer.parseInt(slots[i + 1])) {
//                System.out.println(n + 1 != Integer.parseInt(slots[i + 1]));
//                System.out.println(" i==0  n dứng 1 mình " + n);
                    return n;
                } else if (i + 1 == slots.length && (n - 1 != Integer.parseInt(slots[i - 1])))
//                System.out.println("i cuối mảng n đứng 1 mình "+ n);
                    return n;
                else if ((i != 0) && (n - 1 != Integer.parseInt(slots[i - 1])) && (n + 1 != Integer.parseInt(slots[i + 1]))) {
//                System.out.println("i!=0  n dứng 1 mình " + n);
                    return n;
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ERROR_FORMAT);
        }

        return -1;
    }


}
