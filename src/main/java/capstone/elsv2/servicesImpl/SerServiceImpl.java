package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.service.AddServiceDTO;
import capstone.elsv2.dto.service.ServiceDTO;
import capstone.elsv2.dto.service.ServiceDetailDTO;
import capstone.elsv2.dto.service.UpdateServiceDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Package;
import capstone.elsv2.entities.PackageService;
import capstone.elsv2.repositories.PackageRepository;
import capstone.elsv2.repositories.PackageServiceRepository;
import capstone.elsv2.repositories.ServiceRepository;
import capstone.elsv2.services.SerService;
import org.springframework.beans.factory.annotation.Autowired;
import capstone.elsv2.entities.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class SerServiceImpl implements SerService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PackageServiceRepository packageServiceRepository;
    @Autowired
    private PackageRepository packageRepository;

    @Override
    public Boolean createService(AddServiceDTO addServiceDTO) {
        boolean isCreate = false;
        try {
            Service service = Service.builder()
                    .name(addServiceDTO.getName())
                    .price(addServiceDTO.getPrice())
//                    .imgUrl(addServiceDTO.getImgUrl())
                    .status(StatusCode.ACTIVATE.toString())
                    .duration(addServiceDTO.getDuration())
                    .build();
            serviceRepository.save(service);
            isCreate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCreate;
    }

    @Override
    public Boolean updateService(UpdateServiceDTO updateServiceDTO) {
        Boolean isUpdate = false;
        try {
            Service service = serviceRepository.findById(updateServiceDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
            service.setName(updateServiceDTO.getName());
            service.setPrice(updateServiceDTO.getPrice());
//            service.setImgUrl(updateServiceDTO.getImgUrl());
            service.setDuration(updateServiceDTO.getDuration());
            serviceRepository.save(service);
            isUpdate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdate;
    }

    @Override
    public Boolean activateService(String id) {
        boolean isActivate = false;
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
        if (service.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            service.setStatus(StatusCode.ACTIVATE.toString());
            serviceRepository.save(service);
            isActivate = true;
        }
        return isActivate;
    }

    @Override
    public Boolean deactivateService(String id) {
        boolean isDeactivate = false;
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
        if (service.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            service.setStatus(StatusCode.DEACTIVATE.toString());
            List<PackageService> packageServices = packageServiceRepository.findAllByService_Id(id);
            if (!packageServices.isEmpty()) {
                for (PackageService packageService : packageServices) {
                    if (packageService.getStatus().equals(StatusCode.ACTIVATE.toString())) {
                        packageService.setStatus(StatusCode.DEACTIVATE.toString());
                        packageServiceRepository.save(packageService);
                        Package aPackage = packageRepository.findById(packageService.get_package().getId()).get();
                        aPackage.setPrice(aPackage.getPrice().subtract(service.getPrice()));
                        aPackage.setDuration(Math.round((aPackage.getDuration()-service.getDuration())));
                        packageRepository.save(aPackage);
                    }
                }
            }
            serviceRepository.save(service);
            isDeactivate = true;
        }
        return isDeactivate;
    }

    @Override
    public PageDTO findAllService(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        Page<Service> servicePage = serviceRepository.findAll(pageable);
        List<Service> services = servicePage.getContent();
        for (Service service : services) {
            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .price(service.getPrice())
                    .status(service.getStatus())
                    .duration(service.getDuration())
                    .build();
            serviceDTOS.add(serviceDTO);
        }
        int offset = servicePage.getNumber() * servicePage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(serviceDTOS)
                .pageSize(servicePage.getSize())
                .hasNextPage(servicePage.hasNext())
                .toRecord(offset + servicePage.getNumberOfElements() - 1)
                .fromRecord(offset)
                .totalRecord(servicePage.getTotalElements())
                .pageIndex(servicePage.getNumber() + 1)
                .hasPreviousPage(servicePage.hasPrevious())
                .totalPages(servicePage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public List<ServiceDTO> findAllService() {
        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        List<Service> services = serviceRepository.findAll();
        for (Service service : services) {
            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .price(service.getPrice())
                    .status(service.getStatus())
                    .duration(service.getDuration())
                    .build();
            serviceDTOS.add(serviceDTO);
        }
        return serviceDTOS;
    }

    @Override
    public ServiceDetailDTO findById(String id) {
        ServiceDetailDTO serviceDetailDTO = null;
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy dịch vụ nào"));
        serviceDetailDTO = ServiceDetailDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .duration(service.getDuration())
//                .imgUrl(service.getImgUrl())
                .build();
        return serviceDetailDTO;
    }

}
