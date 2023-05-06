package capstone.elsv2.dto.sitter;

import capstone.elsv2.dto.certificate.AddCertificateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SitterCertificateDTO {
    private String sitterId;
    private List<AddCertificateDTO> certificateDTOList;
}
