package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.certificate.AddCertificateDTO;
import capstone.elsv2.dto.certificate.CertificateDTO;
import capstone.elsv2.dto.certificate.CertificateDetailDTO;
import capstone.elsv2.dto.sitter.SitterCertificateDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.Certificate;
import capstone.elsv2.repositories.AccountRepository;
import capstone.elsv2.repositories.CertificateSitterRepository;
import capstone.elsv2.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateSitterRepository certificateSitterRepository;
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Boolean addCertificate(SitterCertificateDTO sitterCertificateDTO) {
        Boolean isCreate = false;
        Account sitter = accountRepository.findById(sitterCertificateDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy nhân viên nào"));
        List<AddCertificateDTO> certificateDTOList = sitterCertificateDTO.getCertificateDTOList();
        try {
            for (AddCertificateDTO certificateDTO : certificateDTOList) {
                Certificate certificateSitter = Certificate.builder()
                        .title(certificateDTO.getTitle())
                        .organization(certificateDTO.getOrganization())
                        .dateReceived(certificateDTO.getDateReceived())
                        .idNumber(certificateDTO.getCredentialID())
                        .imgUrl(certificateDTO.getCertificateImgUrl())
                        .status(StatusCode.ACTIVATE.toString())
                        .url(certificateDTO.getCredentialURL())
                        .sitterProfile(sitter.getSitterProfile())
                        .build();
                certificateSitterRepository.save(certificateSitter);
            }
            isCreate = true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm chứng chỉ");
        }
        return isCreate;
    }

    @Override
    public List<CertificateDTO> getAllCertificateBySitterId(String sitterId) {
        List<CertificateDTO> certificateDTOList = new ArrayList<>();
        List<Certificate> certificateSitters = certificateSitterRepository.findAllBySitterProfile_Account_IdAndStatus(sitterId, StatusCode.ACTIVATE.toString());
        for (Certificate certificateSitter: certificateSitters) {
            CertificateDTO certificateDTO = CertificateDTO.builder()
                    .id(certificateSitter.getId())
                    .title(certificateSitter.getTitle())
                    .organization(certificateSitter.getOrganization())
                    .dateReceived(certificateSitter.getDateReceived())
                    .credentialID(certificateSitter.getIdNumber())
                    .certificateImgUrl(certificateSitter.getImgUrl())
                    .credentialURL(certificateSitter.getUrl())
                    .build();
            certificateDTOList.add(certificateDTO);
        }
        return certificateDTOList;
    }

    @Override
    public CertificateDetailDTO getCertificate(String id) {
        Certificate certificateSitter = certificateSitterRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404),"Không thể tìm thấy chứng chỉ nào"));
        CertificateDetailDTO certificateDetailDTO = CertificateDetailDTO.builder()
                .id(certificateSitter.getId())
                .title(certificateSitter.getTitle())
                .organization(certificateSitter.getOrganization())
                .dateReceived(certificateSitter.getDateReceived())
                .credentialID(certificateSitter.getIdNumber())
                .certificateImgUrl(certificateSitter.getImgUrl())
                .credentialURL(certificateSitter.getUrl())
                .build();
        return certificateDetailDTO;
    }

    @Override
    public Boolean updateCertificate(CertificateDTO certificateDTO) {
        boolean isUpdate = false;
        Certificate certificateSitter = certificateSitterRepository.findById(certificateDTO.getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404),"Không thể tìm thấy chứng chỉ nào"));
        try {
            certificateSitter.setTitle(certificateDTO.getTitle());
            certificateSitter.setOrganization(certificateDTO.getOrganization());
            certificateSitter.setDateReceived(certificateDTO.getDateReceived());
            certificateSitter.setIdNumber(certificateDTO.getCredentialID());
            certificateSitter.setImgUrl(certificateDTO.getCertificateImgUrl());
            certificateSitter.setUrl(certificateDTO.getCredentialURL());
            certificateSitterRepository.save(certificateSitter);
            isUpdate =true;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể chỉnh sửa chứng chỉ nào");
        }
        return isUpdate;
    }

    @Override
    public Boolean removeCertificate(String id) {
        boolean isRemove = false;
        Certificate certificateSitter = certificateSitterRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404),"Không thể tìm thấy chứng chỉ nào"));
        if(certificateSitter.getStatus().equals(StatusCode.ACTIVATE.toString())){
            certificateSitter.setStatus(StatusCode.DEACTIVATE.toString());
            certificateSitterRepository.save(certificateSitter);
            isRemove = true;
        }else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Chứng chỉ đã được xóa không thể xóa nữa");
        }
        return isRemove ;
    }


}
