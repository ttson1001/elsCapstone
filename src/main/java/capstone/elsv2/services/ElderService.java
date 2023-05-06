package capstone.elsv2.services;

import capstone.elsv2.dto.common.StatisticsDTO;
import capstone.elsv2.dto.elder.*;
import capstone.elsv2.entities.Elder;

import java.util.List;

public interface ElderService {
    Boolean createElder(AddElderDTO addElderDTO);
    Boolean updateElder(UpdateElderDTO updateElderDTO);
    Boolean removeElder(String id);
    Boolean checkElderExist(String idCardNumber);
    List<ElderHealStatusDTO> findAllByCustomerId(String id);
    ElderDetailDTO findById(String id);
    List<ElderDTO> findAllByHealStatusAndCustomerId(String id, String status);

    ElderHaveCustomerDTO findByIdNumberCard(String idNumberCard);

    Boolean addRelationShip(AddRelationshipDTO addRelationshipDTO);

//    StatisticsDTO countElder ();
}
