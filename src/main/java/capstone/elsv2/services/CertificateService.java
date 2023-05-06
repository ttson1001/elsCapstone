package capstone.elsv2.services;

import capstone.elsv2.dto.certificate.CertificateDTO;
import capstone.elsv2.dto.certificate.CertificateDetailDTO;
import capstone.elsv2.dto.sitter.SitterCertificateDTO;

import java.util.List;

public interface CertificateService {
    Boolean addCertificate(SitterCertificateDTO sitterCertificateDTO);

    List<CertificateDTO> getAllCertificateBySitterId(String sitterId);

    CertificateDetailDTO getCertificate(String id);

    Boolean updateCertificate(CertificateDTO certificateDTO);

    Boolean removeCertificate(String id);
}
