package capstone.elsv2.test;

import capstone.elsv2.dto.common.ChartDTO;
import capstone.elsv2.dto.common.SlotDTO;
import capstone.elsv2.dto.mypackage.PackageDTO;
import capstone.elsv2.dto.workingTime.DayAndSlotDTO;
import capstone.elsv2.entities.HolidayDate;
import capstone.elsv2.entities.WorkingTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.text.NumberFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

public class TestMain {
//
//
////    public static void main(String[] args) {
////        String ngayNhap = "2023-06-01";
////        String ngayLe = "2000-06-01";
////
////        // thứ trong một tuần
////        LocalDate LNhap = LocalDate.parse(ngayNhap);
////        LocalDate LLe = LocalDate.parse(ngayLe);
////
////        String ngayThang = LNhap.getMonth().getValue()+"-"+LNhap.getDayOfMonth();
////        String ngayTrongDB = LLe.getMonth().getValue()+"-"+LLe.getDayOfMonth();
////        System.out.println(ngayThang);System.out.println(ngayTrongDB);
////        System.out.println(ngayThang.equals(ngayTrongDB));
////
////        System.out.println(LNhap.getDayOfWeek());
////        System.out.println(LNhap.getDayOfMonth());
////        System.out.println(LNhap.getMonth().getValue());
//
////        System.out.println(BigDecimal.valueOf(10).subtract(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(0.2))));
////        List<String> times = new ArrayList<>();
//
////        String startTime = "2023-06-01T08:32";
////        String endTime = "2023-06-01T10:10";
////        times.add(startTime);
////        times.add(endTime);
////        System.out.println(times.size());
////
////        LocalDateTime localTimeStart = LocalDateTime.parse(startTime);
////        LocalDateTime localTimeEnd = LocalDateTime.parse(endTime);
////
////        System.out.println(localTimeStart);
////        System.out.println(localTimeEnd);
////        System.out.println(localTimeEnd.getDayOfWeek().toString().equals("THURSDAY"));
//
////        Long diffInMin = java.time.Duration.between(localTimeStart,localTimeEnd).toMinutes();
////        System.out.println(diffInMin);
//
////        System.out.println(BigDecimal.valueOf(1));
////        System.out.println(Math.round(1.9));
////
////        Locale localeVN = new Locale("vi", "VN");
////        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
////
////        BigDecimal bigDecimal = BigDecimal.valueOf(3500000);
////        String str1 = currencyVN.format(bigDecimal);
////        System.out.println(str1);
//
////        List<BigDecimal> bigDecimals = new ArrayList<>();
////        bigDecimals.add(BigDecimal.valueOf(1));
////        bigDecimals.add(BigDecimal.valueOf(2));
////        bigDecimals.add(BigDecimal.valueOf(13));
////        bigDecimals.add(BigDecimal.valueOf(5));
////        bigDecimals.add(BigDecimal.valueOf(1));
//
////        bigDecimals.sort(Comparator.comparing(bigDecimal -> bigDecimal));
////        Collections.reverse(bigDecimals);
////
////        for (BigDecimal  bigDecimal: bigDecimals
////             ) {
////            System.out.println(bigDecimal);
////
////        }
////        String thg = "Slot1 00:00 - 02:00";
////        String slotss[] = thg.split(" ");
//////        String slot1 = slot[1];
//////        System.out.println(slot[1].trim());
////        List<SlotDTO> slotDTOS = new ArrayList<>();
////        List<String> slots = new ArrayList<>();
////        slots.add("1 00:00 - 02:00");
////        slots.add("2 02:00 - 04:00");
////        slots.add("3 04:00 - 06:00");
////        slots.add("4 06:00 - 08:00");
////        slots.add("5 08:00 - 10:00");
////        slots.add("6 10:00 - 12:00");
////        slots.add("7 12:00 - 14:00");
////        slots.add("8 14:00 - 16:00");
////        slots.add("9 16:00 - 18:00");
////        slots.add("10 18:00 - 20:00");
////        slots.add("11 20:00 - 22:00");
////        slots.add("12 22:00 - 24:00");
////
////        for (String slot: slots) {
////            SlotDTO slotDTO = SlotDTO.builder()
////                    .slots(slot)
////                    .build();
////            slotDTOS.add(slotDTO);
////        }
////        float s1 = 1; // slot 1 -> 3 slot => 6 tiếng 1 - 2-3
////        //duration-1+slot bắt đầu = 3;
////        //3-1+2 = 4;
////        //3-1+1 = 3;
////        //3-1+10= 12;
////        float s = (float) 5/2;
////        System.out.println(slots.get(1+Math.round(s)-2));
////
////        List<SlotDTO> slotDTOS2= new ArrayList<>();
////        slotDTOS2.add(new SlotDTO("Slot1 00:00 - 02:00"));
//
//
////    String slot = "MONDAY 1-2-3-4-5;TUESDAY 5-6-7-8";
//
////    List<DayAndSlotDTO> dayAndSlotDTOS = new ArrayList<>();
////    String dayAndSlots[] = slot.split(";");
////        for (String dayAndSlot: dayAndSlots) {
////        DayAndSlotDTO dayAndSlotDTO = DayAndSlotDTO
////                .builder()
////                .day(dayAndSlot.split(" ")[0])
////                .slot(dayAndSlot.split(" ")[1])
////                .build();
////        dayAndSlotDTOS.add(dayAndSlotDTO);
////    }
////
////        for (DayAndSlotDTO dayAndSlotDTO: dayAndSlotDTOS) {
////        System.out.println(dayAndSlotDTO.getDay());
////        System.out.println(dayAndSlotDTO.getSlot());
////        System.out.println("-------------------------------------");
////    }
//
//
////    }
//
//
////        String slot = "1-2-4-5-7-8-9-11";
////        String slots[] = slot.split("-");
////        System.out.println(slots.length);
////
////
////        // 1+1 = 2  -> 2 +1 != 5 i= length  tối đa phải là 4 tiếng
////        // 1+1 = 2 -> 2+1 = 3
////        for (int i = 0; i < slots.length; i++) {
////            int n = Integer.parseInt(slots[i]);
////            System.out.println("n " + n);
////            if(i == 0 && n + 1 != Integer.parseInt(slots[i + 1])) {
//////                System.out.println(n + 1 != Integer.parseInt(slots[i + 1]));
////                System.out.println(" i==0  n dứng 1 mình " + n);
////            }else  if(i+1 == slots.length && (n-1 != Integer.parseInt(slots[i-1])))
////                System.out.println("i cuối mảng n đứng 1 mình "+ n);
////            else     if((i!=0) && (n -1 != Integer.parseInt(slots[i-1])) && (n+1 != Integer.parseInt(slots[i + 1]))){
////                System.out.println("i!=0  n dứng 1 mình " + n);
////            }
////        }
//    //
////        String mon = "MONDAY";
////        String tue = "TUESDAY";
////        String wed = "WEDNESDAY";
////        String thu = "THURSDAY";
////        String fri = "FRIDAY";
////        String sat = "SATURDAY";
////        String sun = "SUNDAY";
////
////        List<String> dayOfWeek = new ArrayList<>();
////        dayOfWeek.add(mon);
////        dayOfWeek.add(tue);
////        dayOfWeek.add(wed);
////        dayOfWeek.add(thu);
////        dayOfWeek.add(fri);
////        dayOfWeek.add(sat);
////        dayOfWeek.add(sun);
////
////        List<String> _workingTimes = new ArrayList<>();
////        _workingTimes.add(mon);
////        _workingTimes.add(tue);
////        for (String workingTime: _workingTimes) {
////            for (int i=0; i<dayOfWeek.size();i++) {
////                if(workingTime.equals(dayOfWeek.get(i))){
////                    dayOfWeek.remove(i);
////                }
////            }
////        }
////String dayCan ="";
////        for (String item: dayOfWeek) {
////            dayCan = dayCan + " " + item;
////        }
////        System.out.println(dayCan);
////    }
//
////        String startSlot = "7 22:00-00:00";
////        String StartDate = "2023-04-05";
////        String endDate = "2023-04-05";
//////        12+3-1 = 15-1 = 14 12 13 14
////        int duration = 5;
////        // nếu startSLot = 12
////        // mà duration = 5 lấy duraltion -2 sau đó sẽ 5-2 = 3 thì tính lại từ slot 1 là 1 2 3/2 = 1.5 => 2 slot 1 - 2
////        String endSlot = "3 04:00-06:00";
////        int endSlotIn = 0;
////        float slot = (float) duration / 2;
////        int soSlotCan = Math.round(slot);
////        int s = 0;
////        String slots = "";
////        DayOfWeek dayStartDay = LocalDate.parse(StartDate).getDayOfWeek();
////        DayOfWeek dayEndDay = LocalDate.parse(endDate).getDayOfWeek();
////        String dayStart = dayStartDay + ";";
////        String dayEnd = dayEndDay + ";";
//////        String dayOther = dayEndDay + ";";
////        Set<String> strings = new HashSet<>();
//////        String DayAndSlot;
////        int slotStart = Integer.parseInt(startSlot.split(" ")[0]);
////        if (slotStart + soSlotCan > 12) {
////            for (int i = slotStart; i < slotStart + soSlotCan; i++) {
////                System.out.println(slotStart);
////                if (i > 12) {
////                    s = i - 12;
////                    dayEnd = dayEnd + s + " ";
//////                    dayOther = dayOther + s + " ";
////                } else {
////                    s = i;
////                    dayStart = dayStart + s + " ";
////                }
////                slots = slots + " " + s;
////            }
////        } else {
////            for (int i = slotStart; i < slotStart + soSlotCan; i++) {
////                dayStart = dayStart + i + " ";
////            }
////        }
//////        System.out.println(slots.trim().replace(" ","-"));
////        System.out.println(dayStart.trim().replace(" ", "-"));
////        System.out.println(dayEnd.trim().replace(" ", "-"));
//////        System.out.println(dayOther.trim().replace(" ", "-"));
//////        strings.add(slots.trim().replace(" ","-"));
////        strings.add(dayStart.trim().replace(" ", "-"));
////        System.out.println(!dayEnd.equals(dayEndDay + ";"));
////        if (!dayEnd.equals(dayEndDay + ";")) {
////            strings.add(dayEnd.trim().replace(" ", "-"));
////        }
//////        strings.add(dayOther.trim().replace(" ", "-"));
////
////        System.out.println("/////////////////////////////////");
////        for (String sss : strings) {
////            System.out.println(sss);
////        }
//
////        System.out.println(7*0.2);
//
////        String StartDate = "2023-04-07T00:14";
////
////        LocalDateTime s = LocalDateTime.parse(StartDate);
////        System.out.println(s);
////        System.out.println(LocalDateTime.now().plusHours(36));
////        System.out.println(LocalDateTime.now().plusHours(36).isBefore(s));
////            LocalDate localDate = LocalDate.of(2021, 2, 27);
////            int weekOfYear = localDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
////            System.out.println(weekOfYear);
////        LocalDate date2 = LocalDate.of(2023, 2, 1);
////        Month february = date2.getMonth();// => FEBRUARY
////        LocalDate firstDayOfMonth = date2.with(TemporalAdjusters.firstDayOfMonth());
////        LocalDate lastDayOfMonth = date2.with(TemporalAdjusters.lastDayOfMonth());
////        System.out.println(firstDayOfMonth);
////        System.out.println(lastDayOfMonth);
//
////        List<ChartDTO> integers = new ArrayList<>();
////        integers.add(new ChartDTO(1,BigDecimal.valueOf(10)));
////        integers.add(new ChartDTO(1,BigDecimal.valueOf(10)));
////        integers.add(new ChartDTO(2,BigDecimal.valueOf(10)));
////        integers.add(new ChartDTO(3,BigDecimal.valueOf(10)));
////        integers.add(new ChartDTO(1,BigDecimal.valueOf(10)));
////        integers.add(new ChartDTO(2,BigDecimal.valueOf(10)));
////
////        Map<Integer, BigDecimal> integerIntegerMap = new HashMap<>();
////        for (ChartDTO chartDTO: integers) {
//////            if(integerIntegerMap.containsKey(1))
////                    integerIntegerMap.put(chartDTO.getValue(), BigDecimal.valueOf(0));
////            }
////        for (ChartDTO chartDTO: integers) {
////            if(integerIntegerMap.containsKey(chartDTO.getValue())){
////               integerIntegerMap.put(chartDTO.getValue(), integerIntegerMap.get(chartDTO.getValue()).add(chartDTO.getAmount()));
////            }
////        }
////
////        System.out.println("Initial Mappings are: " + integerIntegerMap);
//
//    //        System.out.println(LocalDate.parse("2023-02-03").with(TemporalAdjusters.lastDayOfMonth()));
//    public static void main(String[] args) {
////        LocalDate localDate1 = LocalDate.parse("2023-02-03");
////        LocalDate localDate2 = LocalDate.parse("2023-02-05");
////        List<LocalDate> dates = new ArrayList<>();
//////        LocalDate startDate = localDate1;
//////        do {
////////
//////            dates.add(startDate);
//////            startDate = startDate.plusDays(1);
////////            System.out.println(localDate2);
////////            System.out.println(startDate);
////////            System.out.println(startDate.equals(localDate2));
//////        } while (!startDate.equals(localDate2.plusDays(1)));
//////
//////        for (LocalDate localDate: dates
//////             ) {
//////            System.out.println(localDate.toString());
////        System.out.println(localDate2.isAfter(localDate1));
//
////        String slot1 = "1-2-3-4";
////        String slot2 = "3-4-5-6";
////        String slot3 = "1-2-3-4-5";
////
////        PackageDTO packageDTO = PackageDTO.builder()
////                .id("1sọadfhk1")
////                .endSlot(1)
////                .startSlot(4)
////                .build();
////        PackageDTO packageDTO2 = PackageDTO.builder()
////                .id("2sọadfhk2")
////                .endSlot(2)
////                .startSlot(3)
////                .build();
////        PackageDTO packageDTO3 = PackageDTO.builder()
////                .id("3sọadfhk3")
////                .endSlot(7)
////                .startSlot(10)
////                .build();
////        List<PackageDTO> packageDTOS = new ArrayList<>();
////        packageDTOS.add(packageDTO);
////        packageDTOS.add(packageDTO2);
////        packageDTOS.add(packageDTO3);
////
////
////        List<String> slots = new ArrayList<>();
////        slots.add(slot1);
////        slots.add(slot2);
////        slots.add(slot3);
////
////        Set<Integer> slot = new HashSet<>();
////        List<PackageDTO> packageDTOS1 = new ArrayList<>();
////
////        for (String s : slots) {
////            for (String h : s.split("-")) {
////                slot.add(Integer.parseInt(h));
////            }
////        }
////        for (PackageDTO packageDTO1 : packageDTOS) {
////            if (slot.contains(packageDTO1.getStartSlot()) &&
////                    slot.contains(packageDTO1.getEndSlot())) {
////                packageDTOS1.add(packageDTO1);
////            }
////
////        }
////        for (PackageDTO packageDTO1: packageDTOS1
////             ) {
////            System.out.println(packageDTO1.getId());
////        }
////        LocalDateTime localDate1 = LocalDateTime.parse("2023-04-19T14:40");
////        Duration duration = Duration.between(LocalDateTime.now(), localDate1);
////        long hour = duration.toMinutes();
////        System.out.println(hour);
////        if(hour<30 && hour>-30) System.out.println("duoc phep check in");
////        else System.out.println("Khoong duoc phep check in");
////        ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
////        System.out.println(LocalDate.now(istZoneId));
//
////        System.out.println(LocalDateTime.parse("2023-04-19T03:00").plusHours(12));
////        System.out.println(LocalDateTime.now());
////        Duration duration = Duration.between(LocalDateTime.now(),LocalDateTime.parse("2023-05-01T15:00"));
////        System.out.println(duration.toMinutes());
//
////        double $latitude1 = 10.851195957993697 ;
////        double $longitude1 = 106.79762958868122;
////        double $latitude2 = 10.841103650773379;
////        double $longitude2 = 106.81000755282136;
////
////        double longi1 = Math.toRadians($longitude1);
////        double longi2 = Math.toRadians($longitude2);
////
////        double lat1 = Math.toRadians($latitude1);
////        double lat2 = Math.toRadians($latitude2);
////
////        double diffLong = longi2 - longi1;
////        double difflat = lat2 - lat1;
////
////        double val = Math.pow(Math.sin(difflat/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(diffLong/2),2);
////        double res1 = 6378* (2 * Math.asin(Math.sqrt(val)));
////
////        System.out.println(res1);
//
////        System.out.println(BigDecimal.valueOf(5000).compareTo(BigDecimal.valueOf(5000)) <0 || BigDecimal.valueOf(50000000).compareTo(BigDecimal.valueOf(5000000)) >0);
//
//    }
//
//
public static void main(String[] args) {
//    LocalDate localDate = LocalDate.now();
//    LocalDateTime localDate1 = LocalDateTime.parse("2023-05-05T14:40");
//    System.out.println(localDate1.toLocalDate().isBefore(localDate));
    System.out.println(new BigDecimal(1.11120319f).setScale(1, RoundingMode.HALF_UP).floatValue());
}
}
