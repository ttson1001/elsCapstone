package capstone.elsv2.mapper;

import capstone.elsv2.dto.booking.response.CustomerResponseDTO;
import capstone.elsv2.entities.CustomerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    default CustomerResponseDTO convertCustomerDTO (CustomerProfile customerProfile){
        return CustomerResponseDTO.builder()
                .id(customerProfile.getId())
                .numberOfCancels(customerProfile.getNumberOfCancels())
                .status(customerProfile.getAccount().getStatus())
                .build() ;
    }
}
