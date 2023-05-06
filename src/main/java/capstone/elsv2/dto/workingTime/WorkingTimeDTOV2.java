package capstone.elsv2.dto.workingTime;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkingTimeDTOV2 {
    private String dayOfWeek; // MON 1-2-3-5-6|TUE
    private int slot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingTimeDTOV2 that = (WorkingTimeDTOV2) o;
        if(dayOfWeek.equals(that.dayOfWeek) && slot == that.slot)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, slot);
    }

}
