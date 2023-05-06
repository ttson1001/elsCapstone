package capstone.elsv2.services;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.promotion.AddPromotionDTO;
import capstone.elsv2.dto.promotion.PromotionDTO;
import capstone.elsv2.dto.promotion.UpdatePromotionDTO;

import java.util.List;

public interface PromotionService {
    Boolean addPromotion (AddPromotionDTO addPromotionDTO);
    PageDTO getAllPromotion(String keyWord, int pageNumber, int pageSize);
    List<PromotionDTO> getAllPromotion();
    Boolean updatePromotion(UpdatePromotionDTO promotionDTO);
    PromotionDTO getPromotion(String id);
    Boolean activePromotion(String id);
    Boolean deActivePromotion(String id);

    List<PromotionDTO> getAllPromotion(String customerID);
}
