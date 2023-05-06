package capstone.elsv2.services;

import capstone.elsv2.dto.workexp.AddWorkExpDTO;
import capstone.elsv2.dto.workexp.UpdateWorkExpDTO;
import capstone.elsv2.dto.workexp.WorkExpDTO;

import java.util.List;

public interface WorkExpService {
    Boolean addWorkExp(AddWorkExpDTO addWorkExpDTO);

    Boolean updateWorkExp(UpdateWorkExpDTO updateWorkExpDTO);

    Boolean removeWorkExp(String id);
    List<WorkExpDTO>  getAllWorkExp(String sitterId);
    WorkExpDTO getWorkExp(String id);
}
