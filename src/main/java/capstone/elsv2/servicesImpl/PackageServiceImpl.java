package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.SlotDTO;
import capstone.elsv2.dto.mypackage.*;
import capstone.elsv2.dto.service.ServiceDTO;
import capstone.elsv2.emunCode.BookingDetailStatus;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.*;
import capstone.elsv2.entities.Package;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.CommonService;
import capstone.elsv2.services.MyPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

@Service
public class PackageServiceImpl implements MyPackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PackageServiceRepository packageServiceRepository;
    @Autowired
    private SitterPackageRepository sitterPackageRepository;
    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;


    @Override
    public List<PackageDTO> getAllPackageByWorkingTimeSitter(String sitterId) {
        List<WorkingTime> workingTimes = workingTimeRepository.findAllBySitter_Id(sitterId);
        Set<Integer> slotWorkingTimes = new HashSet<>();
        for (WorkingTime workingTime : workingTimes) {
            for (String s : workingTime.getSlots().split("-")) {
                slotWorkingTimes.add(Integer.parseInt(s));
            }
        }
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByStatus(StatusCode.ACTIVATE.toString());

        for (Package aPackage : packages) {
            if (slotWorkingTimes.contains(aPackage.getStartSlot()) && slotWorkingTimes.contains(aPackage.getEndSlot())) {
                PackageDTO packageDTO = PackageDTO.builder()
                        .id(aPackage.getId())
                        .name(aPackage.getName())
                        .price(aPackage.getPrice())
                        .startSlot(aPackage.getStartSlot())
                        .endSlot(aPackage.getEndSlot())
                        .duration(aPackage.getDuration())
                        .img(aPackage.getImgUrl())
                        .desc(aPackage.getDescription())
                        .status(aPackage.getStatus())
                        .build();
                packageDTOS.add(packageDTO);
            }
        }


        return packageDTOS;
    }

//    @Override
//    public Boolean createPackage(AddPackageDTO addPackageDTO) {
//        boolean isCreate = false;
//        try {
//            Package aPackage = Package.builder()
//                    .price(BigDecimal.valueOf(0))
//                    .name(addPackageDTO.getName())
//                    .imgUrl(addPackageDTO.getImg())
//                    .status(StatusCode.ACTIVATE.toString())
//                    .description(addPackageDTO.getDesc())
//                    .build();
//            packageRepository.save(aPackage);
//            isCreate = true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isCreate;
//    }


    @Override
    public List<PackageDTO> findAllActivatePackageHaveSitter(String keyword) {
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByStatusAndNameContains(StatusCode.ACTIVATE.toString(), keyword);
        for (Package aPackage : packages) {
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getName())
                    .price(aPackage.getPrice())
                    .startSlot(aPackage.getStartSlot())
                    .endSlot(aPackage.getEndSlot())
                    .duration(aPackage.getDuration())
                    .img(aPackage.getImgUrl())
                    .desc(aPackage.getDescription())
                    .status(aPackage.getStatus())
                    .build();
            packageDTOS.add(packageDTO);
        }
        return packageDTOS;
    }

    @Override
    @Transactional
    public Boolean createPackageV2(AddPackageDTO addPackageDTO) {
        Boolean isCrete = false;
        Package aPackage = null;
        aPackage = Package.builder()
                .price(BigDecimal.valueOf(0))
                .duration(0)
                .name(addPackageDTO.getName())
                .imgUrl(addPackageDTO.getImg())
                .healthStatus(addPackageDTO.getHealthStatus())
                .status(StatusCode.ACTIVATE.toString())
                .startSlot(Integer.parseInt(addPackageDTO.getSlotStart().split(" ")[0]))
                .startTime(LocalTime.parse(addPackageDTO.getSlotStart().split(" ")[1].split("-")[0]))
                .endTime(LocalTime.parse(addPackageDTO.getSlotEnd().split(" ")[1].split("-")[1]))
                .endSlot(Integer.parseInt(addPackageDTO.getSlotEnd().split(" ")[0]))
                .description(addPackageDTO.getDesc())
                .build();
        aPackage = packageRepository.save(aPackage);
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        float duration = 0F;
        List<String> ids = addPackageDTO.getServiceId();
        if (ids.size() > 10)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "KHông thể thêm quá 10 dịch vụ cho một gói");
        if (!ids.isEmpty()) {
            for (String id : ids) {
                capstone.elsv2.entities.Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
                totalPrice = service.getPrice().add(totalPrice);
                duration += service.getDuration();
                PackageService packageService = PackageService.builder()
                        ._package(aPackage)
                        .service(service)
                        .status(StatusCode.ACTIVATE.toString())
                        .build();
                packageServiceRepository.save(packageService);
            }
        }
        aPackage.setDuration(Math.round(duration));
        aPackage.setPrice(totalPrice);
        packageRepository.save(aPackage);
        isCrete = true;
        return isCrete;
    }

    @Override
    public Boolean updatePackage(UpdatePackageDTO updatePackageDTO) {
        boolean isUpdate = false;
        Package aPackage = packageRepository.findById(updatePackageDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào"));
        aPackage.setName(updatePackageDTO.getName());
        aPackage.setImgUrl(updatePackageDTO.getImg());
//        aPackage.setPrice(updatePackageDTO.getPrice());
        aPackage.setStartSlot(Integer.parseInt(updatePackageDTO.getSlotStart().split(" ")[0]));
        aPackage.setEndSlot(Integer.parseInt(updatePackageDTO.getSlotEnd().split(" ")[0]));
        aPackage.setStartTime(LocalTime.parse(updatePackageDTO.getSlotStart().split(" ")[1].split("-")[0]));
        aPackage.setEndTime(LocalTime.parse(updatePackageDTO.getSlotEnd().split(" ")[1].split("-")[1]));
        aPackage.setHealthStatus(updatePackageDTO.getHealthStatus());
        aPackage.setDescription(updatePackageDTO.getDesc());
        if (aPackage != null) {
            packageRepository.save(aPackage);
            isUpdate = true;
        }
        return isUpdate;

    }

    @Override
    public Boolean removePackage(String id) {
        boolean isRemove = false;
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào"));
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAllBy_package_Id(id);
        for (BookingDetail bookingDetail : bookingDetails) {
            if (!bookingDetail.getStatus().equals(BookingDetailStatus.DONE.toString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể hủy gói dịch vụ này vì có booking đang làm việc");
            }
        }
        if (aPackage.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            aPackage.setStatus(StatusCode.DEACTIVATE.toString());
            packageRepository.save(aPackage);
            isRemove = true;
        }
        return isRemove;

    }

    @Override
    public Boolean activePackage(String id) {
        boolean isActive = false;
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào"));
        if (aPackage.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            aPackage.setStatus(StatusCode.ACTIVATE.toString());
            packageRepository.save(aPackage);
            isActive = true;
        }
        return isActive;
    }

    @Override
    public List<PackageDTO> findAllActivatePackage() {
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByStatus(StatusCode.ACTIVATE.toString());
        for (Package aPackage : packages) {
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getName())
                    .price(aPackage.getPrice())
                    .startSlot(aPackage.getStartSlot())
                    .endSlot(aPackage.getEndSlot())
                    .duration(aPackage.getDuration())
                    .img(aPackage.getImgUrl())
                    .desc(aPackage.getDescription())
                    .status(aPackage.getStatus())
                    .build();
            packageDTOS.add(packageDTO);
        }
        return packageDTOS;
    }

    @Override
    public List<PackageDTO> findAllActivatePackageHaveSitter() {
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<SitterPackage> sitterPackages = sitterPackageRepository.findAllByStatus(StatusCode.ACTIVATE.toString());
        Set<String> ids = new HashSet<>();
        for (SitterPackage sitterPackage : sitterPackages) {
            ids.add(sitterPackage.get_package().getId());
        }
        for (String id : ids) {
            Package aPackage = packageRepository.findById(id).get();
            if (aPackage.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                PackageDTO packageDTO = PackageDTO.builder()
                        .id(aPackage.getId())
                        .name(aPackage.getName())
                        .price(aPackage.getPrice())
                        .startSlot(aPackage.getStartSlot())
                        .endSlot(aPackage.getEndSlot())
                        .duration(aPackage.getDuration())
                        .desc(aPackage.getDescription())
                        .status(aPackage.getStatus())
                        .build();
                packageDTOS.add(packageDTO);
            }
        }

        return packageDTOS;
    }

    @Override
    public PageDTO findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<PackageDTO> packageDTOS = new ArrayList<>();
        Page<Package> packagePage = packageRepository.findAll(pageable);
        List<Package> packages = packagePage.getContent();
        for (Package aPackage : packages) {
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getName())
                    .price(aPackage.getPrice())
                    .startSlot(aPackage.getStartSlot())
                    .endSlot(aPackage.getEndSlot())
                    .duration(aPackage.getDuration())
                    .desc(aPackage.getDescription())
                    .status(aPackage.getStatus())
                    .build();
            packageDTOS.add(packageDTO);
        }
        int offset = packagePage.getNumber() * packagePage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(packageDTOS)
                .pageSize(packagePage.getSize())
                .hasNextPage(packagePage.hasNext())
                .pageIndex(packagePage.getNumber() + 1)
                .hasPreviousPage(packagePage.hasPrevious())
                .totalRecord(packagePage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + packagePage.getNumberOfElements() - 1)
                .totalPages(packagePage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public Boolean addServiceToPackage(ServicePackageDTO addServiceToPackageDTO) {
        boolean isAdd = false;
        List<String> ids = addServiceToPackageDTO.getServiceId();
        List<String> serviceIds = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        if (!ids.isEmpty()) {
            Package aPackage = packageRepository.findById(addServiceToPackageDTO.getPackageId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm tháy gói dịch vụ nào"));
            for (String id : ids) {
                capstone.elsv2.entities.Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
                List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(addServiceToPackageDTO.getPackageId());
                for (PackageService packageService : packageServices) {
                    if (packageService.getService().getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.valueOf(400), "Đã có một dịch vụ bị trùng : " + service.getName());
                    }
                }
                totalPrice = service.getPrice().add(totalPrice);
                PackageService packageService = PackageService.builder()
                        ._package(aPackage)
                        .service(service)
                        .status(StatusCode.ACTIVATE.toString())
                        .build();
                packageServiceRepository.save(packageService);
            }
            List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(addServiceToPackageDTO.getPackageId());
            for (PackageService packageService : packageServices) {
                serviceIds.add(packageService.getService().getId());
            }
            aPackage.setPrice(aPackage.getPrice().add(totalPrice));
            aPackage.setStatus(StatusCode.ACTIVATE.toString());
            packageRepository.save(aPackage);
            isAdd = true;
        }
        return isAdd;
    }

    @Override
    public Boolean removeServiceInPackage(ServicePackageDTO removeServiceInPackageDTO) {
        boolean isRemove = false;
        List<String> ids = removeServiceInPackageDTO.getServiceId();
        if (!ids.isEmpty()) {
            Package aPackage = packageRepository.findById(removeServiceInPackageDTO.getPackageId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm tháy gói dịch vụ nào"));
            for (String id : ids) {
                PackageService packageService = packageServiceRepository.findPackageServiceBy_package_IdAndService_Id(removeServiceInPackageDTO.getPackageId(), id);
                capstone.elsv2.entities.Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
                if (packageService.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                    packageService.setStatus(StatusCode.DEACTIVATE.toString());
                    packageServiceRepository.save(packageService);
                    aPackage.setDuration(Math.round(aPackage.getDuration() - service.getDuration()));
                    aPackage.setPrice(aPackage.getPrice().subtract(service.getPrice()));
                    packageRepository.save(aPackage);
                }
            }
            isRemove = true;
            int count = 0;
            List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(aPackage.getId());
            for (PackageService packageService : packageServices) {
                if (packageService.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
                    count += 1;
                }
            }
            if (count == packageServices.size()) {
                aPackage.setStatus(StatusCode.DEACTIVATE.toString());
                aPackage.setDuration(0);
                packageRepository.save(aPackage);
            }
        }
        return isRemove;
    }

    @Override
    public Boolean activeServiceInPackage(ServicePackageDTO activeServiceInPackageDTO) {
        boolean isActive = false;
        List<String> ids = activeServiceInPackageDTO.getServiceId();
        if (!ids.isEmpty()) {
            Package aPackage = packageRepository.findById(activeServiceInPackageDTO.getPackageId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm tháy gói dịch vụ nào"));
            for (String id : ids) {
                PackageService packageService = packageServiceRepository.findPackageServiceBy_package_IdAndService_Id(activeServiceInPackageDTO.getPackageId(), id);
                capstone.elsv2.entities.Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
                if (packageService.getStatus().equals(StatusCode.DEACTIVATE.toString()) && service.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                    packageService.setStatus(StatusCode.ACTIVATE.toString());
                    packageServiceRepository.save(packageService);
                    isActive = true;
                }
                aPackage.setPrice(aPackage.getPrice().add(service.getPrice()));
                aPackage.setDuration(Math.round(aPackage.getDuration() + service.getDuration()));
                packageRepository.save(aPackage);
            }
        }
        return isActive;
    }

    @Override
    public PackageDetailDTO findById(String id) {
        PackageDetailDTO packageDetailDTO = null;
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào"));
        List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(aPackage.getId());
        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        for (PackageService packageService : packageServices) {
            capstone.elsv2.entities.Service service = packageService.getService();
            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .price(service.getPrice())
                    .status(packageService.getStatus())
                    .build();
            serviceDTOS.add(serviceDTO);
        }
        packageDetailDTO = PackageDetailDTO.builder()
                .id(aPackage.getId())
                .img(aPackage.getImgUrl())
                .slotEnd(aPackage.getEndSlot())
                .slotStart(aPackage.getStartSlot())
                .desc(aPackage.getDescription())
                .startTime(aPackage.getStartTime())
                .endTime(aPackage.getEndTime())
                .name(aPackage.getName())
                .duration(aPackage.getDuration())
                .healthStatus(aPackage.getHealthStatus())
                .price(aPackage.getPrice())
                .serviceDTOS(serviceDTOS)
                .build();
        return packageDetailDTO;
    }

    @Override
    public PackageDetailDTO findByIdWithServiceActive(String id) {
        PackageDetailDTO packageDetailDTO = null;
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào"));
        List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(aPackage.getId());
        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        for (PackageService packageService : packageServices) {
            capstone.elsv2.entities.Service service = packageService.getService();
            if (packageService.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                ServiceDTO serviceDTO = ServiceDTO.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .price(service.getPrice())
                        .status(packageService.getStatus())
                        .build();
                serviceDTOS.add(serviceDTO);
            }
        }
        packageDetailDTO = PackageDetailDTO.builder()
                .id(aPackage.getId())
                .img(aPackage.getImgUrl())
                .desc(aPackage.getDescription())
                .name(aPackage.getName())
                .duration(aPackage.getDuration())
                .price(aPackage.getPrice())
                .healthStatus(aPackage.getHealthStatus())
                .serviceDTOS(serviceDTOS)
                .build();
        return packageDetailDTO;
    }

    @Override
    public List<PackageDTO> getRandomPackage(Integer count) {
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByStatus(StatusCode.ACTIVATE.toString());
        count = count - 1;
        if (packages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào");
        }
        Collections.shuffle(packages);
        for (int i = 0; i <= count; i++) {
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(packages.get(i).getId())
                    .name(packages.get(i).getName())
                    .price(packages.get(i).getPrice())
                    .startSlot(packages.get(i).getStartSlot())
                    .endSlot(packages.get(i).getEndSlot())
                    .img(packages.get(i).getImgUrl())
                    .duration(packages.get(i).getDuration())
                    .desc(packages.get(i).getDescription())
                    .status(packages.get(i).getStatus())
                    .build();
            packageDTOS.add(packageDTO);
        }
        return packageDTOS;
    }

    @Override
    public List<PackageDTO> getAllPackageByHealthStatus(String healthStatus) {
        List<PackageDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByHealthStatusAndStatus(healthStatus, StatusCode.ACTIVATE.toString());
        if (packages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào");
        }
        for (Package aPackage : packages) {
            PackageDTO packageDTO = PackageDTO.builder()
                    .id(aPackage.getId())
                    .name(aPackage.getName())
                    .price(aPackage.getPrice())
                    .startSlot(aPackage.getStartSlot())
                    .endSlot(aPackage.getEndSlot())
                    .duration(aPackage.getDuration())
                    .desc(aPackage.getDescription())
                    .status(aPackage.getStatus())
                    .build();
            packageDTOS.add(packageDTO);
        }
        return packageDTOS;

    }

    @Override
    public List<PackageDetailBookingDTO> getAllPackageDetailByHealthStatus(String healthStatus) {
        List<PackageDetailBookingDTO> packageDTOS = new ArrayList<>();
        List<Package> packages = packageRepository.findAllByHealthStatusAndStatus(healthStatus, StatusCode.ACTIVATE.toString());
        if (packages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy gói dịch vụ nào");
        }

        for (Package aPackage : packages) {
            List<PackageService> packageServices = packageServiceRepository.findAllBy_package_Id(aPackage.getId());
            List<String> services = new ArrayList<>();
            for (PackageService packageService : packageServices) {
                capstone.elsv2.entities.Service service = packageService.getService();
                if (packageService.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                    services.add(service.getName());
                }
            }
            PackageDetailBookingDTO packageDetailDTO = PackageDetailBookingDTO.builder()
                    .id(aPackage.getId())
                    .img(aPackage.getImgUrl())
                    .slotEnd(aPackage.getEndSlot())
                    .slotStart(aPackage.getStartSlot())
                    .desc(aPackage.getDescription())
                    .startTime(aPackage.getStartTime())
                    .endTime(aPackage.getEndTime())
                    .name(aPackage.getName())
                    .duration(aPackage.getDuration())
                    .healthStatus(aPackage.getHealthStatus())
                    .price(aPackage.getPrice())
                    .services(services)
                    .build();
            packageDTOS.add(packageDetailDTO);
        }
        return packageDTOS;

    }


}
