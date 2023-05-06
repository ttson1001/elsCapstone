package capstone.elsv2.services;

import capstone.elsv2.dto.common.OrderChatDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.StatisticsDTO;
import capstone.elsv2.dto.customer.*;

import java.util.List;
import java.util.Set;

public interface CusService {
    PageDTO findAllCustomer(int pageNumber, int pageSize);

    CustomerDetailDTO findById(String id);

    Boolean updateCustomer(UpdateCustomerDTO updateCustomerDTO);

    Boolean banCustomer(String id);

    Boolean unBanCustomer(String id);

    Boolean register(CustomerRegisterDTO customerRegisterDTO);

    PageDTO searchByKeyWord(String keyWord,int pageNumber, int pageSize);

//    StatisticsDTO countAllCus();

//    Set<OrderChatDTO> getAllMessageByCustomerId(String customerId);
}
