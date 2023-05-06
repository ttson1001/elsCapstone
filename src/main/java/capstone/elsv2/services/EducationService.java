package capstone.elsv2.services;

import capstone.elsv2.dto.education.AddEducationDTO;
import capstone.elsv2.dto.education.EducationDTO;
import capstone.elsv2.dto.education.EducationDetailDTO;
import capstone.elsv2.dto.education.UpdateEducationDTO;

import java.util.List;

public interface EducationService {
    Boolean addEducation(AddEducationDTO addEducationDTO);
    Boolean updateEducation(UpdateEducationDTO updateEducationDTO);
    List<EducationDTO> findAllBySitterId(String id);
    EducationDetailDTO findById(String id);
    Boolean removeEducation(String id);
}
