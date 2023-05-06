package capstone.elsv2.services;

import capstone.elsv2.dto.common.AddHolidayDateDTO;
import capstone.elsv2.dto.common.UpdateHolidayDateDTO;
import capstone.elsv2.entities.HolidayDate;

import java.util.List;

public interface HolidayDateService {
    HolidayDate addHoliday(AddHolidayDateDTO addHolidayDateDTO);

    List<HolidayDate> getAllHoliday();

    HolidayDate updateHoliday(UpdateHolidayDateDTO updateHolidayDateDTO);
}
