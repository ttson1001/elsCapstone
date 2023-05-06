package capstone.elsv2.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRangeDTO  {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int slot;

    public DateRangeDTO(LocalDateTime startTime,LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime =endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRangeDTO that = (DateRangeDTO) o;
        return startTime.isEqual(that.getStartTime()) && endTime.isEqual(that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, slot);
    }

}
