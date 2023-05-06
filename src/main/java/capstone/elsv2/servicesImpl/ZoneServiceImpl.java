package capstone.elsv2.servicesImpl;

import capstone.elsv2.entities.Zone;
import capstone.elsv2.repositories.ZoneRepository;
import capstone.elsv2.services.ZoneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private ZoneRepository zoneRepository;

    private List<String> getListDistrict(){
            List<String> districts = new ArrayList<>();
            districts.add("Quận 1");
            districts.add("Quận 2");
            districts.add("Quận 3");
            districts.add("Quận 4");
            districts.add("Quận 5");
            districts.add("Quận 6");
            districts.add("Quận 7");
            districts.add("Quận 8");
            districts.add("Quận 9");
            districts.add("Quận 10");
            districts.add("Quận 11");
            districts.add("Quận 12");
            districts.add("Thủ Đức");
            districts.add("Gò Vấp");
            districts.add("Bình Thạnh");
            districts.add("Tân Bình");
            districts.add("Tân Phú");
            districts.add("Phú Nhuận");
            districts.add("Bình Tân");
            districts.add("Củ Chi");
            districts.add("Hóc Môn");
            districts.add("Bình Chánh");
            districts.add("Nhà Bè");
            districts.add("Cần Giờ");
            return districts;
    }
    @Override
    public List<Zone> initZone() {
        List<String> districts = getListDistrict();
        List<Zone> zones = districts.stream()
                .map(x ->Zone.builder().district(x).build())
                .collect(Collectors.toList());
        return zoneRepository.saveAll(zones);
    }
}
