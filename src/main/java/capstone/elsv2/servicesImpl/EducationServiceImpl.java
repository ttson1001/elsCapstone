package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.education.AddEducationDTO;
import capstone.elsv2.dto.education.EducationDTO;
import capstone.elsv2.dto.education.EducationDetailDTO;
import capstone.elsv2.dto.education.UpdateEducationDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.Education;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.EducationRepository;
import capstone.elsv2.services.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Boolean addEducation(AddEducationDTO addEducationDTO) {
        boolean isCreate = false;
        Account sitter = accountRepository.findById(addEducationDTO.getSitterId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404),"Không tìm thấy chăm sóc viên nào"));
        try {
            Education education = Education.builder()
                    .educationLevel(addEducationDTO.getEducationLevel())
                    .major(addEducationDTO.getMajor())
                    .schoolName(addEducationDTO.getSchoolName())
                    .fromDate(addEducationDTO.getFromDate())
                    .endDate(addEducationDTO.getEndDate())
                    .isGraduate(addEducationDTO.getIsGraduate())
                    .GPA(addEducationDTO.getGPA())
                    .status(StatusCode.ACTIVATE.toString())
                    .educationImg(addEducationDTO.getEducationImg())
                    .description(addEducationDTO.getDescription())
                    .sitter(sitter.getSitterProfile())
                    .build();
            educationRepository.save(education);
            isCreate = true;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(400),"Không thể thêm học vấn cho chăm sóc viên");
        }
        return isCreate;
    }

    @Override
    public Boolean updateEducation(UpdateEducationDTO updateEducationDTO) {
        Boolean isUpdate = false;
        Education education = educationRepository.findById(updateEducationDTO.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404),"Không tìm thấy học vấn nào"));
        try {
            education.setEducationLevel(updateEducationDTO.getEducationLevel());
            education.setMajor(updateEducationDTO.getMajor());
            education.setGPA(updateEducationDTO.getGPA());
            education.setSchoolName(updateEducationDTO.getSchoolName());
            education.setFromDate(updateEducationDTO.getFromDate());
            education.setEndDate(updateEducationDTO.getEndDate());
            education.setIsGraduate(updateEducationDTO.getIsGraduate());
            education.setEducationImg(updateEducationDTO.getEducationImg());
            education.setDescription(updateEducationDTO.getDescription());
            educationRepository.save(education);
            isUpdate = true;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(400),"Không thể chỉnh sửa học vấn");
        }
        return isUpdate;
    }

    @Override
    public List<EducationDTO> findAllBySitterId(String id) {
        List<EducationDTO> educationDTOS = new ArrayList<>();
        List<Education> educations = educationRepository.findAllBySitter_IdAndStatus(id,StatusCode.ACTIVATE.toString());
        for (Education education: educations ) {
            EducationDTO educationDTO = EducationDTO.builder()
                    .id(education.getId())
                    .gpa(education.getGPA())
                    .educationLevel(education.getEducationLevel())
                    .isGraduate(education.getIsGraduate())
                    .schoolName(education.getSchoolName())
                    .build();
            educationDTOS.add(educationDTO);
        }
        return educationDTOS;

    }

    @Override
    public EducationDetailDTO findById(String id) {
        Education education = educationRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404),"không tìm thấy học vấn nào"));
        EducationDetailDTO educationDetailDTO = EducationDetailDTO.builder()
                .id(education.getId())
                .educationLevel(education.getEducationLevel())
                .major(education.getMajor())
                .schoolName(education.getSchoolName())
                .fromDate(education.getFromDate())
                .endDate(education.getEndDate())
                .isGraduate(education.getIsGraduate())
                .GPA(education.getGPA())
                .educationImg(education.getEducationImg())
                .description(education.getDescription())
                .build();
        return educationDetailDTO;
    }

    @Override
    public Boolean removeEducation(String id) {
        Boolean isRemove = false;
        Education education = educationRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404),"không tìm thấy học vấn nào"));
        if(education.getStatus().equals(StatusCode.ACTIVATE.toString())){
            education.setStatus(StatusCode.DEACTIVATE.toString());
            educationRepository.save(education);
            isRemove = true;
        }else {
            throw new ResponseStatusException(HttpStatus.valueOf(400),"Không thể xóa nữa");
        }
        return isRemove;
    }


}
