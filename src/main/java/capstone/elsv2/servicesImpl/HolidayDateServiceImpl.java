package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.AddHolidayDateDTO;
import capstone.elsv2.dto.common.UpdateHolidayDateDTO;
import capstone.elsv2.entities.HolidayDate;
import capstone.elsv2.repositories.HolidayDateRepository;
import capstone.elsv2.services.HolidayDateService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayDateServiceImpl implements HolidayDateService {
    private final HolidayDateRepository holidayDateRepository;

    public HolidayDateServiceImpl(HolidayDateRepository holidayDateRepository) {
        this.holidayDateRepository = holidayDateRepository;
    }

    @Override
    public HolidayDate addHoliday(AddHolidayDateDTO addHolidayDateDTO) {
        if(addHolidayDateDTO.getEndDate().isAfter(addHolidayDateDTO.getStartDate())){
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày kết thúc không thể trước ngày bắt đầu");
        }
        HolidayDate holidayDate = HolidayDate.builder()
                .startDate(addHolidayDateDTO.getStartDate())
                .endDate(addHolidayDateDTO.getEndDate())
                .name(addHolidayDateDTO.getName())
                .build();
        holidayDateRepository.save(holidayDate);
        return holidayDate;

    }


    @Override
    public List<HolidayDate> getAllHoliday() {
        List<HolidayDate> holidayDates = holidayDateRepository.findAll();
        return holidayDates;
    }

    @Override
    public HolidayDate updateHoliday(UpdateHolidayDateDTO updateHolidayDateDTO) {
        if(LocalDate.parse(updateHolidayDateDTO.getEndDate()).isBefore(LocalDate.parse(updateHolidayDateDTO.getStartDate()))){
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày kết thúc không thể trước ngày bắt đầu");
        }
        HolidayDate holidayDate = holidayDateRepository.findById(updateHolidayDateDTO.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        holidayDate.setStartDate(LocalDate.parse(updateHolidayDateDTO.getStartDate()));
        holidayDate.setEndDate(LocalDate.parse(updateHolidayDateDTO.getEndDate()));
        holidayDate.setName(updateHolidayDateDTO.getName());
        holidayDateRepository.save(holidayDate);
        return holidayDate;
    }
}
