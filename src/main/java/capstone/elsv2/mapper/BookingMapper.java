package capstone.elsv2.mapper;

import capstone.elsv2.dto.booking.response.BookingResponseDTO;
import capstone.elsv2.dto.booking.response.WaitingBookingResponseDTO;
import capstone.elsv2.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(uses = {CustomerMapper.class})
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingResponseDTO bookingConvertToDTO(Booking booking);

    default WaitingBookingResponseDTO bookingConvertToWaitingDTO (Booking booking){
        return WaitingBookingResponseDTO.builder()
                .bookingId(booking.getId())
                .address(booking.getAddress())
                .startDate(booking.getStartDate())
                .elder(ElderMapper.INSTANCE.convertToDTO(booking.getElder()))
                .aPackage(booking.getBookingDetails().get(0).get_package())
                .slots(booking.getSlots())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
    List<WaitingBookingResponseDTO> bookingConvertToWaitingDTO (List<Booking> booking);
}
