package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.tracking.AddTrackingDTO;
import capstone.elsv2.dto.tracking.TrackingDTO;
import capstone.elsv2.dto.tracking.TrackingResponseDTO;
import capstone.elsv2.entities.BookingDetail;
import capstone.elsv2.entities.Tracking;
import capstone.elsv2.repositories.BookingDetailRepository;
import capstone.elsv2.repositories.TrackingRepository;
import capstone.elsv2.services.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    TrackingRepository trackingRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    private final ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    @Override
    public Boolean addTracking(AddTrackingDTO addTrackingDTO) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(addTrackingDTO.getBookingDetailId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "KHông tìm thấy được ngày làm việc này"));
        Tracking tracking = Tracking.builder()
                .bookingDetail(bookingDetail)
                .image(addTrackingDTO.getImage())
                .note(addTrackingDTO.getNote())
                .time(LocalTime.now(istZoneId))
                .build();
        trackingRepository.save(tracking);
        return true;
    }

    @Override
    public TrackingResponseDTO getTracking(String bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "KHông tìm thấy được ngày làm việc này"));
        List<Tracking> trackings = trackingRepository.findAllByBookingDetail_Id(bookingDetailId);
        List<TrackingDTO> trackingList = new ArrayList<>();
        for (Tracking tracking : trackings) {
            TrackingDTO trackingDTO = TrackingDTO.builder()
                    .time(tracking.getTime().toString())
                    .image(tracking.getImage())
                    .note(tracking.getNote())
                    .build();
            trackingList.add(trackingDTO);
        }
        TrackingResponseDTO responseDTO = TrackingResponseDTO.builder()
                .trackingDTOList(trackingList)
                .date(bookingDetail.getStartDateTime().toString().split("T")[0])
                .build();
        return responseDTO;
    }

    @Override
    public List<TrackingResponseDTO> getAllTracking(String bookingId) {
        List<TrackingResponseDTO> trackingResponseDTOS = new ArrayList<>();
        List<BookingDetail> bookingDetailList = bookingDetailRepository.findAllByBooking_Id(bookingId);
        for (BookingDetail bookingDetail : bookingDetailList) {
            if (!bookingDetail.getTrackingList().isEmpty() || bookingDetail.getStartDateTime().toLocalDate().equals(LocalDate.now(istZoneId)) ) {
                List<Tracking> trackings = trackingRepository.findAllByBookingDetail_Id(bookingDetail.getId());
                List<TrackingDTO> trackingList = new ArrayList<>();
                for (Tracking tracking : trackings) {
                    if (tracking.getImage() == null) tracking.setImage("");
                    TrackingDTO trackingDTO = TrackingDTO.builder()
                            .time(tracking.getTime().toString())
                            .image(tracking.getImage())
                            .note(tracking.getNote())
                            .build();
                    trackingList.add(trackingDTO);
                }
                trackingList.sort(Comparator.comparing(t -> t.getTime()));
                Collections.reverse(trackingList);

                TrackingResponseDTO responseDTO = TrackingResponseDTO.builder()
                        .bookingDetailId(bookingDetail.getId())
                        .trackingDTOList(trackingList)
                        .date(bookingDetail.getStartDateTime().toLocalDate().toString())
                        .build();
                trackingResponseDTOS.add(responseDTO);
            }
        }
        trackingResponseDTOS.sort(Comparator.comparing(t -> t.getDate()));
        return trackingResponseDTOS;
    }
}
