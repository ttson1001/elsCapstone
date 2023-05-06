package capstone.elsv2.services;

import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.sitterpackage.AddSitterPackageDTO;
import capstone.elsv2.entities.SitterPackage;

import java.util.List;
import java.util.Set;

public interface SitterPackageService {
    Boolean addSitterInPackage(AddSitterPackageDTO addSitterPackageDTO);
    List<PackageDTO>findAllPackageBySitterId(String sitterId);
    List<PackageDTO> findAllPackageSitterNotHave(String sitterId);


}
