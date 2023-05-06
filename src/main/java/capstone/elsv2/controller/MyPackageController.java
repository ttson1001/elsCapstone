package capstone.elsv2.controller;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.ResponseDTO;
import capstone.elsv2.dto.mypackage.*;
import capstone.elsv2.emunCode.SuccessCode;
import capstone.elsv2.services.MyPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/package")
public class MyPackageController {

    @Autowired
    private MyPackageService packageService;


//    @PostMapping("admin/create")
//    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
//    public ResponseEntity<ResponseDTO> createPackage(@RequestBody AddPackageDTO addPackageDTO) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        if (packageService.createPackage(addPackageDTO)) {
//            responseDTO.setData(true);
//            responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
//            return ResponseEntity.ok().body(responseDTO);
//        } else {
//            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm gói dịch vụ");
//        }
//    }

    @PostMapping("admin/create/v2")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> createPackageV2(@RequestBody AddPackageDTO addPackageDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.createPackageV2(addPackageDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm gói dịch vụ");
        }
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> updatePackage(@RequestBody UpdatePackageDTO updatePackageDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.updatePackage(updatePackageDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể chỉnh sửa gói dịch vụ");
        }
    }

    @PatchMapping("admin/remove/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> removePackage(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.removePackage(id)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Gói dịch vụ đã bị xóa không thể xóa lần nữa");
        }
    }

    @PatchMapping("admin/active/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> activePackage(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.activePackage(id)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Gói dịch vụ đã được kích hoạt không thể kích hoạt lần nữa");
        }
    }

    @GetMapping("admin/packages/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<PageDTO> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        PageDTO pageDTO = packageService.findAll(pageNumber,pageSize);
        if (pageDTO != null) {
            pageDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
            return ResponseEntity.ok().body(pageDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không có gói dịch vụ nào cả");
        }

    }

    @GetMapping("admin/packages/{keyWord}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> searchPackages(@PathVariable String keyWord) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(packageService.findAllActivatePackageHaveSitter(keyWord));
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("admin/add-service-to-package")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> addServiceToPackage(@RequestBody ServicePackageDTO servicePackageDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.addServiceToPackage(servicePackageDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.ADD_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm dịch vụ vào gói");
        }
    }

    @PatchMapping("admin/deactivate-service-in-package")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> deactivateServiceInPackage(@RequestBody ServicePackageDTO servicePackageDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.removeServiceInPackage(servicePackageDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã có dịch vụ đã được xóa ra khỏi gói nên khg thể xóa nữa");
        }
    }

    @PatchMapping("admin/activate-service-in-package")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> activateServiceInPackage(@RequestBody ServicePackageDTO servicePackageDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (packageService.activeServiceInPackage(servicePackageDTO)) {
            responseDTO.setData(true);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã có dịch vụ đã được kích hoạt nên khg thể kích hoạt nữa");
        }
    }

    @GetMapping("admin/package/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        PackageDetailDTO packageDetailDTO = packageService.findById(id);
        responseDTO.setData(packageDetailDTO);
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/activate-packages")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getAllActivatePackages() {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PackageDTO> packageDTOList = packageService.findAllActivatePackage();
        responseDTO.setData(packageDTOList);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/activate-packages-have-sitter")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getAllActivatePackagesHaveSitter() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(packageService.findAllActivatePackageHaveSitter());
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/package/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> findByIdActiveActivate(@PathVariable String id) {
        ResponseDTO responseDTO = new ResponseDTO();
        PackageDetailDTO packageDetailDTO = packageService.findByIdWithServiceActive(id);
        responseDTO.setData(packageDetailDTO);
        responseDTO.setSuccessCode(SuccessCode.FIND_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("mobile/random_package/{count}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> randomPackage(@PathVariable Integer count) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PackageDTO> PackageDTOs = packageService.getRandomPackage(count);
        responseDTO.setData(PackageDTOs);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-by-healthStatus/{status}")
    @PreAuthorize("hasAnyRole('CUSTOMER','SITTER')")
    public ResponseEntity<ResponseDTO> getByHealthStatus(@PathVariable String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PackageDTO> PackageDTOs = packageService.getAllPackageByHealthStatus(status);
        responseDTO.setData(PackageDTOs);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-by-working-time/{sitterId}")
    @PreAuthorize("hasAnyRole('SITTER')")
    public ResponseEntity<ResponseDTO> getByWorkingTime(@PathVariable String sitterId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PackageDTO> PackageDTOs = packageService.getAllPackageByWorkingTimeSitter(sitterId);
        responseDTO.setData(PackageDTOs);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("mobile/get-all-by-activate-and-healthStatus/{status}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllPackageDetailByHealthStatus(@PathVariable String status) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PackageDetailBookingDTO> PackageDTOs = packageService.getAllPackageDetailByHealthStatus(status);
        responseDTO.setData(PackageDTOs);
        responseDTO.setSuccessCode(SuccessCode.FIND_ALL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }


}
