package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.sitterpackage.AddSitterPackageDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Package;
import capstone.elsv2.entities.PackageService;
import capstone.elsv2.entities.SitterPackage;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.repositories.PackageRepository;
import capstone.elsv2.repositories.SitterPackageRepository;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.services.MyPackageService;
import capstone.elsv2.services.SitterPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SitterPackageServiceImpl implements SitterPackageService {
    @Autowired
    private SitterPackageRepository sitterPackageRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private SitterProfileRepository sitterProfileRepository;

    @Autowired
    private MyPackageService packageService;

    @Override
    public Boolean addSitterInPackage(AddSitterPackageDTO addSitterPackageDTO) {
        Boolean isCreate = false;
        SitterProfile sitterProfile = sitterProfileRepository.findById(addSitterPackageDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc viên"));
        List<String> packageIds = addSitterPackageDTO.getPackageIds();
        List<SitterPackage> sitterPackages = new ArrayList<>();
        for (String id : packageIds) {
            Package aPackage = packageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói nào"));
            if (sitterProfile.getAccount().getStatus().equals(StatusCode.ACTIVATE.toString())) {
                SitterPackage sitterPackage = SitterPackage.builder()
                        .sitterProfile(sitterProfile)
                        .status(StatusCode.ACTIVATE.toString())
                        ._package(aPackage)
                        .build();
                sitterPackages.add(sitterPackage);
            }else {
                SitterPackage sitterPackage = SitterPackage.builder()
                        .sitterProfile(sitterProfile)
                        .status(StatusCode.DEACTIVATE.toString())
                        ._package(aPackage)
                        .build();
                sitterPackages.add(sitterPackage);
            }
        }
        sitterPackageRepository.saveAll(sitterPackages);
        return true;
    }

    @Override
    public List<PackageDTO> findAllPackageBySitterId(String sitterId) {
        sitterProfileRepository.findById(sitterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<SitterPackage> sitterPackages = sitterPackageRepository.findAllBySitterProfile_Id(sitterId);
        if (!sitterPackages.isEmpty()) {
            for (SitterPackage sitterPackage : sitterPackages) {
                Package aPackage = packageRepository.findById(sitterPackage.get_package().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói nào"));
                PackageDTO packageDTO = PackageDTO.builder()
                        .id(aPackage.getId())
                        .name(aPackage.getName())
                        .desc(aPackage.getDescription())
                        .img(aPackage.getImgUrl())
                        .price(aPackage.getPrice())
                        .status(aPackage.getStatus())
                        .duration(aPackage.getDuration())
                        .build();
                packageDTOS.add(packageDTO);
            }
        }
        return packageDTOS;
    }

    @Override
    public List<PackageDTO> findAllPackageSitterNotHave(String sitterId) {
        sitterProfileRepository.findById(sitterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<PackageDTO> packageDTOS = packageService.findAllActivatePackage();
        List<PackageDTO> packageDTOList = findAllPackageBySitterId(sitterId);
        List<PackageDTO> packageDTOS1 = new ArrayList<>();
        for (PackageDTO packageDTO : packageDTOS) {
            int count = 0;
            for (PackageDTO _packageDTO : packageDTOList) {
                if (packageDTO.getId().equals(_packageDTO.getId())) {
                    count++;
                }
            }
            if (count == 0) packageDTOS1.add(packageDTO);
        }
        return packageDTOS1;
    }
}
