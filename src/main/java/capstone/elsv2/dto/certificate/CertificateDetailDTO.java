package capstone.elsv2.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CertificateDetailDTO {
    private String id;
    private String title;
    private String organization;
    private LocalDate dateReceived;
    private String credentialID;
    private String credentialURL;
    private String certificateImgUrl;
}
