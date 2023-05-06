package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.StatisticsDTO;
import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.elder.*;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.CustomerProfile;
import capstone.elsv2.entities.Elder;
import capstone.elsv2.entities.Relationship;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElderServiceImpl implements ElderService {
    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Override
    public Boolean createElder(AddElderDTO addElderDTO) {
        boolean isCreate = false;
        Account accountCus = accountRepository.findById(addElderDTO.getIdCustomer()).get();
        Elder elder = null;
        elder = elderRepository.findByIdCardNumber(addElderDTO.getIdCardNumber());
        if (elder != null) {
            List<Relationship> relationships =  elder.getRelationships();
            if(relationships.size() > 4)   throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã có 4 người đăng kí cho người thân này bạn không thể đăng kí cho người thân này nữa");
            for (Relationship relationship : relationships) {
                if(accountCus.getId().equals(relationship.getCustomer().getId())){
                    throw new ResponseStatusException(HttpStatus.valueOf(400), "Người thân này đã được đăng kí vui lòng kiểm tra lại");
                }
            }
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã có người thân trong hệ thống vui lòng xác nhận có phải là người thân không");
        }
        try {
            elder = elderRepository.findByIdCardNumber(addElderDTO.getIdCardNumber());
            if (elder == null) {
                elder = Elder.builder()
                        .fullName(addElderDTO.getFullName())
                        .dob(addElderDTO.getDob())
                        .note(addElderDTO.getNote())
                        .idCardNumber(addElderDTO.getIdCardNumber())
                        .healthStatus(addElderDTO.getHealthStatus())
                        .gender(addElderDTO.getGender())
                        .status(StatusCode.ACTIVATE.toString())
                        .build();
                elder = elderRepository.save(elder);

                Relationship relationship = Relationship.builder()
                        .customer(accountCus.getCustomerProfile())
                        .elder(elder)
                        .build();
                relationshipRepository.save(relationship);
                isCreate = true;
            } else {
                elder = elderRepository.findByIdCardNumber(addElderDTO.getIdCardNumber());
                Relationship relationship = Relationship.builder()
                        .customer(accountCus.getCustomerProfile())
                        .elder(elder)
                        .build();
                relationshipRepository.save(relationship);
                isCreate = true;
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm người người thân");
        }
        return isCreate;
    }

    @Override
    public Boolean updateElder(UpdateElderDTO updateElderDTO) {
        boolean isCreate = false;
        Elder elder = elderRepository.findById(updateElderDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người thân nào"));
        elder.setFullName(updateElderDTO.getFullName());
        elder.setNote(updateElderDTO.getNote());
        elder.setDob(updateElderDTO.getDob());
        elder.setHealthStatus(updateElderDTO.getHealthStatus());
        if (elder.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            elderRepository.save(elder);
            isCreate = true;
        }
        return isCreate;
    }

    @Override
    public Boolean removeElder(String id) {
        boolean isRemove = false;
        Elder elder = elderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người thân nào"));
        if (elder.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            elder.setStatus(StatusCode.DEACTIVATE.toString());
            elderRepository.save(elder);
            isRemove = true;
        }
        System.out.println(isRemove);
        return isRemove;
    }

    @Override
    public Boolean checkElderExist(String idCardNumber) {
        Elder elder = elderRepository.findByIdCardNumber(idCardNumber);
        if (elder == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Chưa có elder nào như vậy trông hệ thống");
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(200), "Đã có elder trong hệ thống vui lòng xác nhận có phải là người thân không");
        }
    }

    @Override
    public List<ElderHealStatusDTO> findAllByCustomerId(String id) {
        List<ElderHealStatusDTO> elderDTOS = new ArrayList<>();
        List<Relationship> relationships = relationshipRepository.findByCustomer_Id(id);
        List<Elder> elders = new ArrayList<>();
        for (Relationship relationship : relationships) {
            Elder elder = relationship.getElder();
            if (elder.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                elders.add(elder);
            }
        }
        for (Elder elder : elders) {
            ElderHealStatusDTO elderDTO = ElderHealStatusDTO.builder()
                    .id(elder.getId())
                    .dob(elder.getDob())
                    .fullName(elder.getFullName())
                    .healthStatus(elder.getHealthStatus())
                    .gender(elder.getGender())
                    .build();
            elderDTOS.add(elderDTO);
        }
        return elderDTOS;
    }
//
    @Override
    public ElderDetailDTO findById(String id) {
        ElderDetailDTO elderDetailDTO = null;
        Elder elder = elderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người thân nào"));
        elderDetailDTO = ElderDetailDTO.builder()
                .fullName(elder.getFullName())
                .dob(elder.getDob())
                .idCardNumber(elder.getIdCardNumber())
                .healthStatus(elder.getHealthStatus())
                .note(elder.getNote())
                .gender(elder.getGender())
                .build();
        return elderDetailDTO;
    }

//    @Override
//    public StatisticsDTO countElder() {
//        StatisticsDTO statisticsDTO = new StatisticsDTO();
//        statisticsDTO.setTotal(elderRepository.findAllByStatus(StatusCode.ACTIVATE.toString()).size());
//        return statisticsDTO;
//    }

    @Override
    public List<ElderDTO> findAllByHealStatusAndCustomerId(String id, String status) {
        List<ElderDTO> elderDTOS = new ArrayList<>();
        List<Relationship> relationships = relationshipRepository.findByCustomer_Id(id);
        List<Elder> elders = new ArrayList<>();
        for (Relationship relationship : relationships) {
            Elder elder = relationship.getElder();
            if (elder.getStatus().equals(StatusCode.ACTIVATE.toString()) && elder.getHealthStatus().equals(status)) {
                elders.add(elder);
            }
        }
        for (Elder elder : elders) {
            ElderDTO elderDTO = ElderDTO.builder()
                    .id(elder.getId())
                    .dob(elder.getDob())
                    .fullName(elder.getFullName())
                    .gender(elder.getGender())
                    .build();
            elderDTOS.add(elderDTO);
        }
        if (elders.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy người người thân nào phù hợp với gói");
        }
        return elderDTOS;
    }

    @Override
    public ElderHaveCustomerDTO findByIdNumberCard(String idCardNumber) {
        Elder elder = elderRepository.findByIdCardNumber(idCardNumber);
        List<Relationship> relationships = relationshipRepository.findAllByElder_Id(elder.getId());
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        List<String> cusIds = new ArrayList<>();
        for (Relationship relationship: relationships) {
            cusIds.add(relationship.getCustomer().getAccount().getId());
//                        System.out.println(relationship.getCustomer().getAccount().getId());
        }
        for(String id : cusIds){
            CustomerProfile customerProfile = customerProfileRepository.findById(id).get();

            CustomerDTO customerDTO = CustomerDTO.builder()
                    .fullName(customerProfile.getAccount().getFullName())
                    .phone(customerProfile.getAccount().getPhone())
                    .address(customerProfile.getAddress())
                    .email(customerProfile.getAccount().getEmail())
                    .gender(customerProfile.getGender())
                    .id(customerProfile.getId())
                    .build();
            customerDTOList.add(customerDTO);
        }
        ElderHaveCustomerDTO elderHaveCustomerDTO = ElderHaveCustomerDTO.builder()
                .id(elder.getId())
                .customerDTOList(customerDTOList)
                .dob(elder.getDob())
                .gender(elder.getGender())
                .fullName(elder.getFullName())
                .healthStatus(elder.getHealthStatus())
                .build();
    return elderHaveCustomerDTO;
    }


    @Override
    @Transactional
    public Boolean addRelationShip(AddRelationshipDTO addRelationshipDTO) {
        CustomerProfile customerProfile = customerProfileRepository.findById(addRelationshipDTO.getCustomerId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy khach hàng nào"));
        Elder elder = elderRepository.findById(addRelationshipDTO.getElderId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy người thân nào"));
        Relationship relationship = Relationship.builder()
                .customer(customerProfile)
                .elder(elder)
                .build();
        relationshipRepository.save(relationship);
        return true;
    }
}
