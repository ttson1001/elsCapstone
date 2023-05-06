//package capstone.elsv2.utils;
//
//import capstone.elsv2.dto.location.CityDTO;
//import capstone.elsv2.dto.location.DistrictDTO;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ListLocation {
//
//    public CityDTO getCity() {
//        CityDTO cityDTO = CityDTO.builder()
//                .name("Thành phố Hồ Chí Minh")
//                .code(79)
//                .codename("thanh_pho_ho_chi_minh")
//                .division_type("thành phố trung ương")
//                .phone_code(28)
//                .build();
//        return cityDTO;
//    }
//
//    public List<DistrictDTO> findAllWardByDistrict(String districtName) {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        if (districtName.equalsIgnoreCase("Quận 1")) {
//            districtDTOS = getListWard760();
//        }
//        if (districtName.equalsIgnoreCase("Quận 12")) {
//            districtDTOS = getListWard761();
//        }
//        if (districtName.equalsIgnoreCase("Quận Gò Vấp")) {
//            districtDTOS = getListWard764();
//        }
//        if (districtName.equalsIgnoreCase("Quận Bình Thạnh")) {
//            districtDTOS = getListWard765();
//        }
//        if (districtName.equalsIgnoreCase("Quận Tân Bình")) {
//            districtDTOS = getListWard766();
//        }
//        if (districtName.equalsIgnoreCase("Quận Tân Phú")) {
//            districtDTOS = getListWard767();
//        }
//        if (districtName.equalsIgnoreCase("Quận Phú Nhuận")) {
//            districtDTOS = getListWard768();
//        }
//        if (districtName.equalsIgnoreCase("Thành phố Thủ Đức")) {
//            districtDTOS = getListWard769();
//        }
//        if (districtName.equalsIgnoreCase("Quận 3")) {
//            districtDTOS = getListWard770();
//        }
//        if (districtName.equalsIgnoreCase("Quận 10")) {
//            districtDTOS = getListWard771();
//        }
//        if (districtName.equalsIgnoreCase("Quận 11")) {
//            districtDTOS = getListWard772();
//        }
//        if (districtName.equalsIgnoreCase("Quận 4")) {
//            districtDTOS = getListWard773();
//        }
//        if (districtName.equalsIgnoreCase("Quận 5")) {
//            districtDTOS = getListWard774();
//        }
//        if (districtName.equalsIgnoreCase("Quận 6")) {
//            districtDTOS = getListWard775();
//        }
//        if (districtName.equalsIgnoreCase("Quận 8")) {
//            districtDTOS = getListWard776();
//        }
//        if (districtName.equalsIgnoreCase("Quận Bình Tân")) {
//            districtDTOS = getListWard777();
//        }
//        if (districtName.equalsIgnoreCase("Quận 7")) {
//            districtDTOS = getListWard778();
//        }
//        if (districtName.equalsIgnoreCase("Huyện Củ Chi")) {
//            districtDTOS = getListWard783();
//        }
//        if (districtName.equalsIgnoreCase("Huyện Hóc Môn")) {
//            districtDTOS = getListWard784();
//        }
//        if (districtName.equalsIgnoreCase("Huyện Bình Chánh")) {
//            districtDTOS = getListWard785();
//        }
//        if (districtName.equalsIgnoreCase("Huyện Nhà Bè")) {
//            districtDTOS = getListWard786();
//        }
//        if (districtName.equalsIgnoreCase("Huyện Cần Giờ")) {
//            districtDTOS = getListWard787();
//        }
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListDistrict() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Quận 1", 760, "quận", "quan_1", 79));
//        districtDTOS.add(new DistrictDTO("Quận 12", 761, "quận", "quan_12", 79));
//        districtDTOS.add(new DistrictDTO("Quận Gò Vấp", 764, "quận", "quan_go_vap", 79));
//        districtDTOS.add(new DistrictDTO("Quận Bình Thạnh", 765, "quận", "quan_binh_thanh", 79));
//        districtDTOS.add(new DistrictDTO("Quận Tân Bình", 766, "quận", "quan_tan_binh", 79));
//        districtDTOS.add(new DistrictDTO("Quận Tân Phú", 767, "quận", "quan_tan_phu", 79));
//        districtDTOS.add(new DistrictDTO("Quận Phú Nhuận", 768, "quận", "quan_phu_nhuan", 79));
//        districtDTOS.add(new DistrictDTO("Thành phố Thủ Đức", 769, "quận", "thanh_pho_thu_duc", 79));
//        districtDTOS.add(new DistrictDTO("Quận 3", 770, "quận", "quan_3", 79));
//        districtDTOS.add(new DistrictDTO("Quận 10", 771, "quận", "quan_10", 79));
//        districtDTOS.add(new DistrictDTO("Quận 11", 772, "quận", "quan_11", 79));
//        districtDTOS.add(new DistrictDTO("Quận 4", 773, "quận", "quan_4", 79));
//        districtDTOS.add(new DistrictDTO("Quận 5", 774, "quận", "quan_5", 79));
//        districtDTOS.add(new DistrictDTO("Quận 6", 775, "quận", "quan_6", 79));
//        districtDTOS.add(new DistrictDTO("Quận 8", 776, "quận", "quan_8", 79));
//        districtDTOS.add(new DistrictDTO("Quận Bình Tân", 777, "quận", "quan_binh_tan", 79));
//        districtDTOS.add(new DistrictDTO("Quận 7", 778, "quận", "quan_7", 79));
//        districtDTOS.add(new DistrictDTO("Huyện Củ Chi", 783, "huyện", "huyen_cu_chi", 79));
//        districtDTOS.add(new DistrictDTO("Huyện Hóc Môn", 784, "huyện", "huyen_hoc_mon", 79));
//        districtDTOS.add(new DistrictDTO("Huyện Bình Chánh", 785, "huyện", "huyen_binh_chanh", 79));
//        districtDTOS.add(new DistrictDTO("Huyện Nhà Bè", 786, "huyện", "huyen_can_gio", 79));
//        districtDTOS.add(new DistrictDTO("Huyện Cần Giờ", 787, "huyện", "huyen_can_gio", 79));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard760() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Tân Định", 26734, "phường", "phuong_tan_dinh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Đa Kao", 26737, "phường", "phuong_da_kao", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bến Nghé", 26740, "phường", "phuong_ben_nghe", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bến Thành", 26743, "phường", "phuong_ben_thanh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Nguyễn Thái Bình", 26746, "phường", "phuong_nguyen_thai_binh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phạm Ngũ Lão", 26749, "phường", "phuong_pham_ngu_lao", 760));
//        districtDTOS.add(new DistrictDTO("Phường Cầu Ông Lãnh", 26752, "phường", "phuong_cau_ong_lanh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Cô Giang", 26755, "phường", "phuong_co_giang", 760));
//        districtDTOS.add(new DistrictDTO("Phường Nguyễn Cư Trinh", 26758, "phường", "phuong_nguyen_cu_trinh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Cầu Kho", 26761, "phường", "phuong_cau_ong_lanh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Cầu Ông Lãnh", 26752, "phường", "phuong_cau_kho", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard761() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Thạnh Xuân", 26764, "phường", "phuong_thanh_xuan", 760));
//        districtDTOS.add(new DistrictDTO("Phường Thạnh Lộc", 26767, "phường", "phuong_thanh_loc", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hiệp Thành", 26770, "phường", "phuong_hiep_thanh", 760));
//        districtDTOS.add(new DistrictDTO("Phường Thới An", 26773, "phường", "phuong_thoi_an", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Chánh Hiệp", 26776, "phường", "phuong_tan_chanh_hiep", 760));
//        districtDTOS.add(new DistrictDTO("Phường An Phú Đông", 26779, "phường", "phuong_an_phu_dong", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Thới Hiệp", 26782, "phường", "phuong_tan_thoi_hiep", 760));
//        districtDTOS.add(new DistrictDTO("Phường Trung Mỹ Tây", 26785, "phường", "phuong_trung_my_tay", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Hưng Thuận", 26787, "phường", "phuong_tan_hung_thuan", 760));
//        districtDTOS.add(new DistrictDTO("Phường Đông Hưng Thuận", 26788, "phường", "phuong_dong_hung_thuan", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Thới Nhất", 26791, "phường", "phuong_tan_thoi_nhat", 760));
//        return districtDTOS;
//    }
//
//
//    public List<DistrictDTO> getListWard764() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 15", 26764, "phường", "phuong_15", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "phuong_13", 760));
//        districtDTOS.add(new DistrictDTO("Phường 17", 26770, "phường", "phuong_17", 760));
//        districtDTOS.add(new DistrictDTO("Phường 6", 26773, "phường", "phuong_6", 760));
//        districtDTOS.add(new DistrictDTO("Phường 16", 26776, "phường", "phuong_16", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26779, "phường", "phuong_12", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26782, "phường", "phuong_14", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26785, "phường", "phuong_10", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26787, "phường", "phuong_05", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26788, "phường", "phuong_07", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26791, "phường", "phuong_04", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26791, "phường", "phuong_01", 760));
//        districtDTOS.add(new DistrictDTO("Phường 9", 26791, "phường", "phuong_9", 760));
//        districtDTOS.add(new DistrictDTO("Phường 8", 26791, "phường", "phuong_8", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26791, "phường", "phuong_11", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26791, "phường", "phuong_03", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard765() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "phuong_13", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26770, "phường", "phuong_11", 760));
//        districtDTOS.add(new DistrictDTO("Phường 27", 26773, "phường", "phuong_27", 760));
//        districtDTOS.add(new DistrictDTO("Phường 26", 26776, "phường", "phuong_26", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26779, "phường", "phuong_12", 760));
//        districtDTOS.add(new DistrictDTO("Phường 25", 26782, "phường", "phuong_25", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26787, "phường", "phuong_05", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26788, "phường", "phuong_07", 760));
//        districtDTOS.add(new DistrictDTO("Phường 24", 26791, "phường", "phuong_24", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26791, "phường", "phuong_06", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26791, "phường", "phuong_14", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26791, "phường", "phuong_15", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26791, "phường", "phuong_02", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26791, "phường", "phuong_03", 760));
//        districtDTOS.add(new DistrictDTO("Phường 17", 26791, "phường", "phuong_17", 760));
//        districtDTOS.add(new DistrictDTO("Phường 21", 26791, "phường", "phuong_21", 760));
//        districtDTOS.add(new DistrictDTO("Phường 22", 26791, "phường", "phuong_22", 760));
//        districtDTOS.add(new DistrictDTO("Phường 19", 26791, "phường", "phuong_19", 760));
//        districtDTOS.add(new DistrictDTO("Phường 28", 26791, "phường", "phuong_28", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard766() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26770, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26773, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26776, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26779, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 25", 26782, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26787, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26788, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26791, "phường", "", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard767() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Tân Sơn Nhì", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tây Thạnh", 26770, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Sơn Kỳ", 26773, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Quý", 26776, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Thành", 26779, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phú Thọ Hòa", 26782, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phú Thạnh", 26787, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phú Trung", 26788, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hòa Thạnh", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hiệp Tân", 26791, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Thới Hòa", 26791, "phường", "", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard768() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 17", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard769() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Linh Xuân", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Chiểu", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Chiểu", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tam Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tam Phú", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hiệp Bình Phước", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hiệp Bình Chánh", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Linh Chiểu", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Linh Tây", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Linh Đông", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Thọ", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Trường Thọ", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Long Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Long Thạnh Mỹ", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Phú", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Hiệp Phú", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tăng Nhơn Phú A", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tăng Nhơn Phú B", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Long B", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Long A", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Trường Thạnh", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Long Phước", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Long Trường", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phước Bình", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường An Khánh", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Trưng Đông", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Trưng Tây", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Cát Lái", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Thạnh Mỹ Lợi", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường An Lợi Đông", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Thủ Thiêm", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard770() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Võ Thị Sáu", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard771() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 15", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard772() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 15", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 16", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard773() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 18", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 16", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard774() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard775() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard776() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường 08", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 02", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 01", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 03", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 11", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 09", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 10", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 04", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 13", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 12", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 05", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 14", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 06", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 15", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 16", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường 07", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard777() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Bình Hưng Hòa", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Hưng Hoà A", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Hưng Hoà B", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Trị Đông", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Trị Đông A", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Trị Đông B", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Tạo", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Tạo A", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường An Lạc", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường An Lạc A", 26767, "phường", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard778() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Phường Tân Thuận Đông", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Thuận Tây", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Kiển", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Hưng", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Bình Thuận", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Quy", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phú Thuận", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Phú", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Tân Phong", 26767, "phường", "", 760));
//        districtDTOS.add(new DistrictDTO("Phường Phú Mỹ", 26767, "phường", "", 760));
//
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard783() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Thị trấn Củ Chi", 26767, "thị trấn", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phú Mỹ Hưng", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã An Phú", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Trung Lập Thượng", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã An Nhơn Tây", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Nhuận Đức", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phạm Văn Cội", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phú Hòa Đông", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Trung Lập Hạ", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Trung An", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phước Thạnh", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phước Hiệp", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân An Hội", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phước Vĩnh An", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Thái Mỹ", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Thạnh Tây", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Hòa Phú", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Thạnh Đông", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bình Mỹ", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Phú Trung", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Thông Hội", 26767, "xã", "", 760));
//
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard784() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Thị trấn Hóc Môn", 26767, "thị trấn", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Hiệp", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Nhị Bình", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Đông Thạnh", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Thới Nhì", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Thới Tam Thôn", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Xuân Thới Sơn", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Xuân", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Xuân Thới Đông", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Trung Chánh", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Xuân Thới Thượng", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bà Điểm", 26767, "xã", "", 760));
//
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard785() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Thị trấn Tân Túc", 26767, "thị trấn", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phạm Văn Hai", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Vĩnh Lộc A", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Vĩnh Lộc B", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bình Lợi", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Lê Minh Xuân", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Nhựt", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tân Kiên", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bình Hưng", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phong Phú", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã An Phú Tây", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Hưng Long", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Đa Phước", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Đa Phước", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bình Chánh", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Quy Đức", 26767, "xã", "", 760));
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard786() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Thị trấn Nhà Bè", 26767, "thị trấn", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phước Kiển", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phước Lộc", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Nhơn Đức", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Phú Xuân", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Long Thới", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Hiệp Phước", 26767, "xã", "", 760));
//
//
//        return districtDTOS;
//    }
//
//    public List<DistrictDTO> getListWard787() {
//        List<DistrictDTO> districtDTOS = new ArrayList<>();
//        districtDTOS.add(new DistrictDTO("Thị trấn Cần Thạnh", 26767, "thị trấn", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Bình Khánh", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Tam Thôn Hiệp", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã An Thới Đông", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Thạnh An", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Long Hòa", 26767, "xã", "", 760));
//        districtDTOS.add(new DistrictDTO("Xã Lý Nhơn", 26767, "xã", "", 760));
//
//
//        return districtDTOS;
//    }
//
//
//}
