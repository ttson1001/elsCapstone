package capstone.elsv2.dto.sitter;

import capstone.elsv2.dto.certificate.CertificateDTO;
import capstone.elsv2.dto.education.EducationDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SitterDetailDTO {
    private String id;
    // info
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private String gender;
    private String address;
    private LocalDate dob;
    private String status;
    private String reason;
    // img
    private String avatarImgUrl;
    private String backCardImgUrl;
    private String frontCardImgUrl;
    // #
    private LocalDate createDate;
    private String tokenDevice;
    private String description;
    private String idCardNumber;
    // fk
    private List<CertificateDTO> certificateDTOList;
    private List<EducationDTO> educationDTOS;
    private List<PackageDTO> packageDTOS;

}
