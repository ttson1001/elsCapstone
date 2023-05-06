package capstone.elsv2.services;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.service.AddServiceDTO;
import capstone.elsv2.dto.service.ServiceDTO;
import capstone.elsv2.dto.service.ServiceDetailDTO;
import capstone.elsv2.dto.service.UpdateServiceDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SerService {
    Boolean createService(AddServiceDTO addServiceDTO);

    Boolean updateService(UpdateServiceDTO updateServiceDTO);

    Boolean activateService(String id);

    Boolean deactivateService(String id);

    PageDTO findAllService(int pageNumber, int pageSize);
    List<ServiceDTO> findAllService();

    ServiceDetailDTO findById(String id);
}
