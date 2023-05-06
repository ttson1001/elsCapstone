package capstone.elsv2.dto.elder;

import capstone.elsv2.dto.customer.CustomerDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ElderHaveCustomerDTO {
    private String id;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String healthStatus;
    private List<CustomerDTO> customerDTOList;
}
