package capstone.elsv2.services;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.mypackage.*;

import java.util.List;

public interface MyPackageService {
 List<PackageDTO> getAllPackageByWorkingTimeSitter(String sitterId);
 Boolean createPackageV2(AddPackageDTO addPackageDTO);
 Boolean updatePackage(UpdatePackageDTO updatePackageDTO);
 Boolean removePackage(String id);
 Boolean activePackage(String id);
 PageDTO findAll(int pageNumber, int pageSize);
 List<PackageDTO> findAllActivatePackage();
 List<PackageDTO> findAllActivatePackageHaveSitter();
 List<PackageDTO> findAllActivatePackageHaveSitter(String keyword);
 List<PackageDetailBookingDTO> getAllPackageDetailByHealthStatus(String healthStatus);
 Boolean addServiceToPackage(ServicePackageDTO addServiceToPackageDTO);
 Boolean removeServiceInPackage(ServicePackageDTO removeServiceInPackageDTO);
 Boolean activeServiceInPackage(ServicePackageDTO activeServiceInPackageDTO);
 PackageDetailDTO findById(String id);
 PackageDetailDTO findByIdWithServiceActive(String id);
 List<PackageDTO> getRandomPackage(Integer count);
 List<PackageDTO> getAllPackageByHealthStatus(String healthStatus);

}
