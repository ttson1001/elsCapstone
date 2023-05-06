package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.rating.AddRatingDTO;
import capstone.elsv2.dto.rating.RatingDTO;
import capstone.elsv2.dto.rating.RatingDetailDTO;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.FeedBack;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.repositories.BookingRepository;
import capstone.elsv2.repositories.RatingRepository;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SitterProfileRepository sitterProfileRepository;

    @Override
    public Boolean addRating(AddRatingDTO addRatingDTO) {
        Booking booking = bookingRepository.findById(addRatingDTO.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy booking nào cả"));
        SitterProfile sitter = sitterProfileRepository.findById(booking.getSitter().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy chăm sóc nào cả"));
        try {
            FeedBack rating = FeedBack.builder()
                    .rate(addRatingDTO.getRate())
                    .comment(addRatingDTO.getComment())
                    .booking(booking)
                    .hashTag(addRatingDTO.getHagTag())
                    .sitter(sitter)
                    .build();
            ratingRepository.save(rating);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Không thể chấm điểm cho sitter");
        }
        return true;
    }

    @Override
    public List<RatingDTO> getAllRatingBySitterId(String sitterId) {
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        List<FeedBack> ratings = ratingRepository.findAllBySitter_Id(sitterId);
        if (ratings.isEmpty()) throw new ResponseStatusException(HttpStatus.valueOf(404), "Không có đánh giá nào");
        for (FeedBack rating : ratings) {
            RatingDTO ratingDTO = RatingDTO.builder()
                    .id(rating.getId())
                    .rate(rating.getRate())
                    .bookingId(rating.getBooking().getId())
                    .comment(rating.getComment())
                    .build();
            ratingDTOS.add(ratingDTO);
        }
        return ratingDTOS;
    }

    @Override
    public RatingDetailDTO getRatingBySitterId(String sitterId) {
        RatingDetailDTO ratingDetailDTO;
        int countRate1 = 0;
        int countRate2 = 0;
        int countRate3 = 0;
        int countRate4 = 0;
        int countRate5 = 0;
        int totalRate = 0;
        String hagTagDTO = "";
        Set<String> hagTags = new HashSet<>();
        List<FeedBack> ratings = ratingRepository.findAllBySitter_Id(sitterId);
        for (FeedBack rating : ratings) {
            if (rating.getRate() == 1) countRate1 += 1;
            if (rating.getRate() == 2) countRate2 += 1;
            if (rating.getRate() == 3) countRate3 += 1;
            if (rating.getRate() == 4) countRate4 += 1;
            if (rating.getRate() == 5) countRate5 += 1;
            String hagTag[] = rating.getHashTag().split(",");
            for (String hag : hagTag) {
                hagTags.add(hag);
            }
            totalRate += rating.getRate();
        }

        for (String h : hagTags) {
            hagTagDTO = hagTagDTO + h + ",";
        }
        String hagT = "";
        if(!hagTagDTO.equals("")){
            hagT = hagTagDTO.substring(0, hagTagDTO.length() - 1);
        }
        ratingDetailDTO = RatingDetailDTO.builder()
                .countOneRate(countRate1)
                .countTwoRate(countRate2)
                .countThreeRate(countRate3)
                .countFourRate(countRate4)
                .countFiveRate(countRate5)
                .average((float) totalRate / ratings.size())
                .hasTag(hagT)
                .build();
        return ratingDetailDTO;


    }
}
