package capstone.elsv2.utils;

import capstone.elsv2.dto.common.DateRangeDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidateUtil {
    public static final String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static boolean isValidDateRange(LocalDate dateStart, LocalDate dateEnd){
        try{
            if(dateEnd.isBefore(dateStart)){
                return false;
            }
            return true;
        }catch (Exception e){

            return false;
        }
    }

    public static boolean isValidDateTimeRange(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd){
        try{
            if(dateTimeEnd.isBefore(dateTimeStart)){
                return false;
            }
            return true;
        }catch (Exception e){

            return false;
        }
    }

    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String && ((String) object).equalsIgnoreCase("")) {
            return true;
        }

        if (object instanceof List<?> && ((List<?>) object).isEmpty()) {
            return true;
        }

        if (object instanceof Set<?> && ((Set<?>) object).isEmpty()) {
            return true;

        }
        return object instanceof Map<?, ?> && ((Map<?, ?>) object).size() == 0;
    }

    public static boolean isOverlap(Set<DateRangeDTO> dateListCheck, Set<DateRangeDTO> dateListCheck2) {
        return !Collections.disjoint(dateListCheck, dateListCheck2);
    }
}
