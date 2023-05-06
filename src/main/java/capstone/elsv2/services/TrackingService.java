package capstone.elsv2.services;

import capstone.elsv2.dto.tracking.AddTrackingDTO;
import capstone.elsv2.dto.tracking.TrackingResponseDTO;

import java.util.List;

public interface TrackingService {
    Boolean addTracking(AddTrackingDTO addTrackingDTO);

    TrackingResponseDTO getTracking(String bookingDetailId);

    List<TrackingResponseDTO> getAllTracking(String bookingId);


}
