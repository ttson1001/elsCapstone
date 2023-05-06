package capstone.elsv2.dto.elder;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddRelationshipDTO {
    private String customerId;
    private String elderId;
}
