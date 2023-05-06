package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.promotion.AddPromotionDTO;
import capstone.elsv2.dto.promotion.PromotionDTO;
import capstone.elsv2.dto.promotion.UpdatePromotionDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.Promotion;
import capstone.elsv2.repositories.BookingDetailRepository;
import capstone.elsv2.repositories.BookingRepository;
import capstone.elsv2.repositories.PromotionRepository;
import capstone.elsv2.sercurity.Utilities;
import capstone.elsv2.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private BookingRepository bookingRepository;


    @Override
    public Boolean addPromotion(AddPromotionDTO addPromotionDTO) {
        if (!LocalDate.parse(addPromotionDTO.getEndDate()).isAfter(LocalDate.parse(addPromotionDTO.getStartDate()))) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày kết thúc không thể trước ngày bắt đầu");
        }


        try {
            Promotion promotion = Promotion.builder()
                    .name(addPromotionDTO.getName())
                    .code(Utilities.randomAlphaNumeric(8))
                    .description(addPromotionDTO.getDescription())
                    .endDate(LocalDate.parse(addPromotionDTO.getEndDate()))
                    .image(addPromotionDTO.getImage())
                    .value(addPromotionDTO.getValue())
                    .startDate(LocalDate.parse(addPromotionDTO.getStartDate()))
                    .status(StatusCode.ACTIVATE.toString())
                    .build();
            promotionRepository.save(promotion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể thêm giảm giá");
        }
        return true;
    }

    @Override
    public PageDTO getAllPromotion(String keyWord, int pageNumber, int pageSize) {
        List<PromotionDTO> promotionDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Promotion> promotionPage;
        if (keyWord.equals("")) {
            promotionPage = promotionRepository.findAll(pageable);
        } else {
            promotionPage = promotionRepository.findAllByKeyWord(pageable, keyWord);
        }
        List<Promotion> promotions = promotionPage.getContent();
        for (Promotion promotion : promotions) {
            PromotionDTO promotionDTO = PromotionDTO.builder()
                    .id(promotion.getId())
                    .name(promotion.getName())
                    .value(promotion.getValue())
                    .code(promotion.getCode())
                    .image(promotion.getImage())
                    .description(promotion.getDescription())
                    .startDate(promotion.getStartDate())
                    .endDate(promotion.getEndDate())
                    .status(promotion.getStatus())
                    .build();
            promotionDTOS.add(promotionDTO);
        }
        int offset = promotionPage.getNumber() * promotionPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(promotionDTOS)
                .pageSize(promotionPage.getSize())
                .hasNextPage(promotionPage.hasNext())
                .pageIndex(promotionPage.getNumber() + 1)
                .totalRecord(promotionPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + promotionPage.getNumberOfElements() - 1)
                .hasPreviousPage(promotionPage.hasPrevious())
                .totalPages(promotionPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public List<PromotionDTO> getAllPromotion() {
        List<PromotionDTO> promotionDTOS = new ArrayList<>();
        List<Promotion> promotions = promotionRepository.findAll();
        for (Promotion promotion : promotions) {
            PromotionDTO promotionDTO = PromotionDTO.builder()
                    .id(promotion.getId())
                    .name(promotion.getName())
                    .value(promotion.getValue())
                    .code(promotion.getCode())
                    .image(promotion.getImage())
                    .description(promotion.getDescription())
                    .startDate(promotion.getStartDate())
                    .endDate(promotion.getEndDate())
                    .status(promotion.getStatus())
                    .build();
            promotionDTOS.add(promotionDTO);
        }
        return promotionDTOS;
    }

    @Override
    public Boolean updatePromotion(UpdatePromotionDTO promotionDTO) {
        if (LocalDate.parse(promotionDTO.getEndDate()).isBefore(LocalDate.parse(promotionDTO.getStartDate()))) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Ngày kết thúc không thể trước ngày bắt đầu");
        }
        Promotion promotion = promotionRepository.findById(promotionDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khuyến mãi nào"));
        promotion.setName(promotionDTO.getName());
        promotion.setStartDate(LocalDate.parse(promotionDTO.getStartDate()));
        promotion.setEndDate(LocalDate.parse(promotionDTO.getEndDate()));
        promotion.setValue(promotionDTO.getValue());
        promotion.setDescription(promotionDTO.getDescription());
        promotionRepository.save(promotion);
        return true;
    }

    @Override
    public PromotionDTO getPromotion(String id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khuyến mãi nào"));
        PromotionDTO promotionDTO = PromotionDTO.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .value(promotion.getValue())
                .code(promotion.getCode())
                .image(promotion.getImage())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .status(promotion.getStatus())
                .description(promotion.getDescription())
                .build();
        return promotionDTO;
    }

    @Override
    public Boolean activePromotion(String id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khuyến mãi nào"));
        if (promotion.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            promotion.setStatus(StatusCode.ACTIVATE.toString());
            promotionRepository.save(promotion);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), " Khuyến mãi đã bị kích hoạt không thể kích hoạt thêm lần nữa");
        }
        return true;
    }

    @Override
    public Boolean deActivePromotion(String id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khuyến mãi nào"));
        if (promotion.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            promotion.setStatus(StatusCode.DEACTIVATE.toString());
            promotionRepository.save(promotion);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Khuyến mãi đã bị hủy không thể hủy thêm lần nữa");
        }
        return true;
    }

    @Override
    public List<PromotionDTO> getAllPromotion(String customerID) {
        List<Promotion> promotionList = promotionRepository.findAllByStatus(StatusCode.ACTIVATE.toString());
        List<PromotionDTO> promotionDTOS = new ArrayList<>();

        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerID);
        for (Booking booking : bookings) {
            promotionList.remove(booking.getPromotion());
        }

        for (Promotion promotion : promotionList) {
            PromotionDTO promotionDTO = PromotionDTO.builder()
                    .id(promotion.getId())
                    .name(promotion.getName())
                    .value(promotion.getValue())
                    .code(promotion.getCode())
                    .image(promotion.getImage())
                    .startDate(promotion.getStartDate())
                    .endDate(promotion.getEndDate())
                    .status(promotion.getStatus())
                    .build();
            promotionDTOS.add(promotionDTO);
        }
        return promotionDTOS;

    }
}
