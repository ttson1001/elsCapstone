package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.workexp.AddWorkExpDTO;
import capstone.elsv2.dto.workexp.UpdateWorkExpDTO;
import capstone.elsv2.dto.workexp.WorkExpDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.WorkExperience;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.WorkExpRepository;
import capstone.elsv2.services.WorkExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkExpServiceImpl implements WorkExpService {

    @Autowired
    private WorkExpRepository workExpRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Boolean addWorkExp(AddWorkExpDTO addWorkExpDTO) {
        boolean isCreate = false;
        Account sitter = accountRepository.findById(addWorkExpDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy được chăm sóc viên "));
        try {
            WorkExperience workExperience = WorkExperience.builder()
                    .name(addWorkExpDTO.getName())
                    .expTime(addWorkExpDTO.getExpTime())
                    .description(addWorkExpDTO.getDescription())
                    .sitter(sitter.getSitterProfile())
                    .status(StatusCode.ACTIVATE.toString())
                    .build();
            workExpRepository.save(workExperience);
            isCreate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm được kinh nghiệm làm việc");
        }
        return isCreate;
    }

    @Override
    public Boolean updateWorkExp(UpdateWorkExpDTO updateWorkExpDTO) {
        boolean isUpdate = false;
        WorkExperience workExperience = workExpRepository.findById(updateWorkExpDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm được kinh nghiệm nào"));
        try {
            workExperience.setName(updateWorkExpDTO.getName());
            workExperience.setExpTime(updateWorkExpDTO.getExpTime());
            workExperience.setDescription(updateWorkExpDTO.getDescription());
            workExpRepository.save(workExperience);
            isUpdate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm được kinh nghiệm làm việc");
        }
        return isUpdate;
    }

    @Override
    public Boolean removeWorkExp(String id) {
        boolean isRemove = false;
        WorkExperience workExperience = workExpRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm được kinh nghiệm nào"));
        if (workExperience.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            workExperience.setStatus(StatusCode.DEACTIVATE.toString());
            workExpRepository.save(workExperience);
            isRemove = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã xóa kinh nghiệm làm việc không thể xóa nữa");
        }
        return isRemove;
    }

    @Override
    public List<WorkExpDTO> getAllWorkExp(String sitterId) {
        List<WorkExpDTO> workExpDTOS = new ArrayList<>();
        List<WorkExperience> workExperiences = workExpRepository.findAllBySitter_IdAndStatus(sitterId, StatusCode.ACTIVATE.toString());
        if (workExperiences.isEmpty()) {
            return workExpDTOS;
        } else {
            for (WorkExperience workExperience : workExperiences) {
                WorkExpDTO workExpDTO = WorkExpDTO.builder()
                        .id(workExperience.getId())
                        .name(workExperience.getName())
                        .description(workExperience.getDescription())
                        .expTime(workExperience.getExpTime())
                        .build();
                workExpDTOS.add(workExpDTO);
            }
        }
        return workExpDTOS;
    }

    @Override
    public WorkExpDTO getWorkExp(String id) {
        WorkExperience workExperience = workExpRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm được kinh nghiệm nào"));
        WorkExpDTO workExpDTO = WorkExpDTO.builder()
                .id(workExperience.getId())
                .name(workExperience.getName())
                .description(workExperience.getDescription())
                .expTime(workExperience.getExpTime())
                .build();
        return workExpDTO;
    }


}
