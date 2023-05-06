package capstone.elsv2.mapper;

import capstone.elsv2.dto.workingTime.WorkingTimeDTO;
import capstone.elsv2.entities.WorkingTime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WorkingTimeMapper {
    WorkingTimeMapper INSTANCE = Mappers.getMapper(WorkingTimeMapper.class);

    WorkingTimeDTO convertWorkingTimeDTO (WorkingTime workingTime);

    List<WorkingTimeDTO> convertWorkingTimeDTOList (List<WorkingTime> workingTimeList);
}
