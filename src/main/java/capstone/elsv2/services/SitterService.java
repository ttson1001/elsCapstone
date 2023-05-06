package capstone.elsv2.services;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.sitter.*;

public interface SitterService {
    PageDTO findAllSitter(int pageNumber, int pageSize);

    PageDTO findAllSitterByKeyWord(String keyWord, int pageNumber, int pageSize);

    Boolean banSitter(String id);

    Boolean unBanSitter(String id);

    Boolean register(SitterRegisterDTO sitterRegisterDTO);

    Boolean updateContact(UpdateSitterContactDTO updateSitterContactDTO);

    SitterContactDTO getContact(String sitterId);

    Boolean updateInformation(SitterInformationDTO sitterInformationDTO);

    SitterInformationDTO getInformation(String sitterId);

    PageDTO findAllSitterForAdmin(int pageNumber, int pageSize);

    PageDTO getAllFormSitter(String status,int pageNumber, int pageSize);

    SitterDetailDTO findSitterById(String id);

    Boolean acceptSitter(String  id);

    Boolean rejectSitter(RejectSitterDTO rejectSitterDTO);

    Boolean sendForm(String id);

    PageDTO findAllSitterReject(int pageNumber, int pageSize);

//    Set<OrderChatDTO> getAllMessageBySitterId(String sitterId);

}
