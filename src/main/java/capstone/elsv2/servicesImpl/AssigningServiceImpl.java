package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.booking.BookingScheduleSitterDTO;
import capstone.elsv2.dto.booking.request.AcceptBookingRequestDTO;
import capstone.elsv2.dto.booking.response.WaitingBookingResponseDTO;
import capstone.elsv2.dto.common.DateRangeDTO;
import capstone.elsv2.dto.common.SlotDTOV2;
import capstone.elsv2.dto.sitter.SitterPriorityDTO;
import capstone.elsv2.dto.workingTime.WorkingTimeDTO;
import capstone.elsv2.dto.workingTime.WorkingTimeDTOV2;
import capstone.elsv2.emunCode.BookingStatus;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.mapper.BookingMapper;
import capstone.elsv2.repositories.BookingRepository;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.services.*;
import capstone.elsv2.utils.ValidateUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssigningServiceImpl implements AssigningService {
    /*
    *Key: sitterId
    * Value : Set<BookingId> waiting for sitter approve */
    private final Map<String, Set<String>> sitterAssignedBookingMap = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<Response>> sitterBookingCompletableFuture = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<Response>> sitterSuccessFail = new ConcurrentHashMap<>();
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private SitterProfileRepository sitterProfileRepository;
    private NotificationService notificationService;
    private CommonService commonService;
    private WorkingTimeService workingTimeService;
    private Map<Integer, SlotDTOV2> slotSystem;
    private enum Response{ACCEPT,DENY,STOP,SUCCESS,FAIL}

    @PostConstruct
    public void init(){
        slotSystem = commonService.getAllSlotMap();
    }

    @Override
    @Async("processExecutor")
    public void assignSitter(Booking booking, List<SitterProfile> listSitterAssign) {
        //priority
        Queue<SitterPriorityDTO> sitterPriority = getPriorityQueue(listSitterAssign,booking);
        if(sitterPriority.size()==0){
            bookingService.cancelBooking(booking);
        }
        //not add to priority
        Queue<SitterPriorityDTO> sitterQueue = new LinkedList<>(sitterPriority);

        String bookingId= booking.getId();
        boolean isTimeout = true;
        boolean isCancel = false;
        long startTime = System.currentTimeMillis();
        long timeout = 30*60*1000; // timeout in 30 mins =  milliseconds

        while((System.currentTimeMillis() - startTime) < timeout){

            //both empty -> no sitter -> cancel
            if(sitterQueue.size() == 0 && sitterPriority.size() ==0){
                isCancel=true;
                break;
            }

            //one empty -> deny -> get sitter priority
            if(sitterQueue.size() == 0){
                if(!assignSitterDirectly(sitterPriority,booking)){
                    isCancel=true;
                }
                isTimeout = false;
                break;
            }

            SitterPriorityDTO sitterPriorityDTO= sitterQueue.remove();
            sitterPriorityDTO.setCount(sitterPriorityDTO.getCount()-1);

            String sitterId = sitterPriorityDTO.getSitterId();
            String key = bookingId+":"+sitterId;


            //after loop 2 time end-to-end queue -> assign most priority
            //if fail all queue -> cancel booking
            if(sitterPriorityDTO.getCount() == 0){
                if(!assignSitterDirectly(sitterPriority,booking)){
                    isCancel=true;
                }
                isTimeout = false;
                break;
            }

            try {
                //send request to sitter
                System.out.println("Send request to sitter: "+sitterId);

                CompletableFuture<Response> responseSitter = new CompletableFuture<>();

                //set up again list waiting assigned booking for sitter
                addBookingIdToWaitingListSitter(sitterId,bookingId);
                //wait for response
                sitterBookingCompletableFuture.put(key,responseSitter);
                notificationService.sendNotificationWithLink(sitterId,
                        "Thông báo",
                        "Bạn có một yêu cầu booking chờ phản hồi",
                        "https://sitterapp.page.link/accountScreen");

                Response response = responseSitter.get(2*60+3, TimeUnit.SECONDS);
                CompletableFuture<Response> successFail = sitterSuccessFail.get(bookingId+":"+sitterId);
                if(response.equals(Response.ACCEPT)){
                    try {
                        addSitterToBooking(bookingId, sitterId);
                        successFail.complete(Response.SUCCESS);
                        isTimeout=false;
                        break;
                    }catch (Exception e){
                        successFail.complete(Response.FAIL);
                        removeBookingIdToWaitingListSitter(sitterId,bookingId);
                        sitterBookingCompletableFuture.remove(key);
                        continue;
                    }
                }
                if(response.equals(Response.STOP)){
                    isTimeout=false;
                    break;
                }
                if(response.equals(Response.DENY)){
                    successFail.complete(Response.SUCCESS);
                }
                removeBookingIdToWaitingListSitter(sitterId,bookingId);
                sitterBookingCompletableFuture.remove(key);

            }catch (ExecutionException | TimeoutException e){
                sitterQueue.add(sitterPriorityDTO);
                removeBookingIdToWaitingListSitter(sitterId,bookingId);
                sitterBookingCompletableFuture.remove(key);
            } catch (InterruptedException e) {
                removeBookingIdToWaitingListSitter(sitterId,bookingId);
                sitterBookingCompletableFuture.remove(key);
            }
        }

        if(isTimeout){
            //assign most priority
            if(!assignSitterDirectly(sitterPriority,booking)){
                isCancel =true;
            }
        }

        if (isCancel){
            bookingService.cancelBooking(booking);
        }
    }

    @Override
    public Boolean acceptSitter(AcceptBookingRequestDTO dto) {
        Boolean isDone = false;
        String key = dto.getBookingId()+":"+ dto.getSitterId();
        CompletableFuture<Response> futureRequest =sitterBookingCompletableFuture.get(key);
        if(futureRequest!=null){
            Response response = dto.getIsAccept()?Response.ACCEPT:Response.DENY;
            futureRequest.complete(response);
            CompletableFuture<Response> successFail = new CompletableFuture<>();

            //wait for response
            sitterSuccessFail.put(key,successFail);
            try {
                Response responseSitter = successFail.get(1*60, TimeUnit.SECONDS);
                isDone= responseSitter.equals(Response.SUCCESS);
            } catch (InterruptedException|ExecutionException|TimeoutException e) {
               isDone =false;
            }
        }
        return isDone;
    }

    @Override
    public List<WaitingBookingResponseDTO> getListWaitingBooking(String sitterId) {
        if(sitterAssignedBookingMap.containsKey(sitterId)){
            Set<String> bookingIds = sitterAssignedBookingMap.get(sitterId);
            List<Booking> bookingList= new ArrayList<>();
            for (String bookingId: bookingIds){
                Booking booking = bookingRepository.getById(bookingId);
                bookingList.add(booking);
            }
            return BookingMapper.INSTANCE.bookingConvertToWaitingDTO(bookingList);
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean stopAssignQueue(Booking booking) {
        Boolean isStop = false;
        for (String key : sitterBookingCompletableFuture.keySet()) {
            if (key.contains(booking.getId())) {
                CompletableFuture<Response> futureRequest =sitterBookingCompletableFuture.get(key);
                String sitterId = key.substring(key.lastIndexOf(":") + 1);
                if(futureRequest!=null){
                    futureRequest.complete(Response.STOP);
                    removeBookingIdToWaitingListSitter(sitterId,booking.getId());
                    isStop=true;
                }
            }
        }
        return isStop;
    }

    @Transactional(rollbackOn = {Exception.class})
    public Booking addSitterToBooking(String bookingId, String sitterId){
        //get Booking
        //validate request time out or not
//        Set<String> bookingIds = sitterAssignedBookingMap.get(sitterId);
//        if(ValidateUtil.isNullOrEmpty(bookingIds) || !bookingIds.contains(bookingId)){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request time out");
//        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking không tồn tại"));

        if(!booking.getStatus().equals(BookingStatus.PENDING.toString())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái booking không phù hợp");
        }
        //check sitter available again for sure (in case many booking request to sitter and sitter accept one of this)
        SitterProfile sitter = sitterProfileRepository.findById(sitterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "CSV không tồn tại"));

        if(!isAvailableSitter(sitter.getId(),booking)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CSV bận");
        }
        //add sitter to booking
        booking.setSitter(sitter);
        booking.setStatus(BookingStatus.ASSIGNED.toString());

        //notify sitter
        notificationService.sendNotification(sitter.getId(), "Thông báo", "Bạn đã dc phân công vào một đơn vui lòng vào ứng dụng để xem thêm thông tin");
        //notify customer
        notificationService.sendNotification(booking.getCustomer().getId(), "Thông báo", "Đã có chăm sóc viên nhận đơn vui lòng vào ứng dụng để xem thêm thông tin ");

        removeBookingIdToWaitingListSitter(sitterId,bookingId);
        return bookingRepository.save(booking);
    }

    @Override
    public Boolean isAvailableSitter(String sitterId, Booking booking){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Set<DateRangeDTO> dateBookingDetailSet = new HashSet<>(); //list compare to booking time in which sitter assigned
        Set<WorkingTimeDTOV2> listBookingWorking = new HashSet<>(); //list compare to working time sitter
        for (String x : booking.getSlots().split(";")) {
            String date = x.split("_")[0];
            int slot = Integer.parseInt(x.split("_")[1]);
            LocalDate dateBooking = LocalDate.parse(date, formatter);

            LocalDateTime dateTimeStart = LocalDateTime.of(dateBooking, slotSystem.get(slot).getStartTime());
            LocalDateTime dateTimeEnd = LocalDateTime.of(dateBooking, slotSystem.get(slot).getEndTime());
            DateRangeDTO dateRangeDTO = new DateRangeDTO(dateTimeStart,dateTimeEnd,slot);
            dateBookingDetailSet.add(dateRangeDTO);
            listBookingWorking.add(new WorkingTimeDTOV2(dateRangeDTO.getStartTime().getDayOfWeek().toString(),dateRangeDTO.getSlot()));
        }

        List<BookingScheduleSitterDTO> listBookingSchedule = bookingService.getAllBookingScheduleSitter(sitterId);

        Set<DateRangeDTO> dateSitterBookingSet = new HashSet<>();
        for (BookingScheduleSitterDTO scheduleSitterDTO: listBookingSchedule){
            LocalDateTime dateTimeStart = scheduleSitterDTO.getStartDateTime();
            LocalDateTime dateTimeEnd = scheduleSitterDTO.getEndDateTime();
            while (dateTimeStart.isBefore(dateTimeEnd)) {
                dateSitterBookingSet.add(new DateRangeDTO(dateTimeStart, dateTimeStart.plusHours(2),getSlotBaseDate(dateTimeStart)));
                dateTimeStart = dateTimeStart.plusHours(2);
            }
        }

        //check time booking sitter assignedr overlap list booking
        if (ValidateUtil.isOverlap(new HashSet<>(dateBookingDetailSet), dateSitterBookingSet)) {
            return false;
        }

        //get list working time
        List<WorkingTimeDTO> listSitterWorking = workingTimeService.getAllActivateWorkingTime(sitterId);
        Set<WorkingTimeDTOV2> listSitterWorkingCheck = new HashSet<>();
        for (WorkingTimeDTO workingTime : listSitterWorking) {
            if (workingTime.getSlots() == null || workingTime.getSlots().isEmpty()) {
                break;
            }
            List<String> listSlot = List.of(workingTime.getSlots().split("-"));
            for (String slot : listSlot) {
                listSitterWorkingCheck.add(new WorkingTimeDTOV2(workingTime.getDayOfWeek(), Integer.parseInt(slot)));
            }
        }

        if (listSitterWorkingCheck.isEmpty()) {
            return false;
        }
        return true;
    }

    private void addBookingIdToWaitingListSitter(String sitterId,String bookingId){
        synchronized (sitterAssignedBookingMap){
            if(sitterAssignedBookingMap.containsKey(sitterId)){
                Set<String> bookingIds = sitterAssignedBookingMap.get(sitterId);
                bookingIds.add(bookingId);
                sitterAssignedBookingMap.put(sitterId,bookingIds);
            }else {
                sitterAssignedBookingMap.put(sitterId,new HashSet<>(Collections.singletonList(bookingId)));
            }
        }
    }

    private void removeBookingIdToWaitingListSitter(String sitterId, String bookingId){
        synchronized (sitterAssignedBookingMap){
            if(sitterAssignedBookingMap.containsKey(sitterId)){
                Set<String> bookingIds = sitterAssignedBookingMap.get(sitterId);
                bookingIds.remove(bookingId);
                sitterAssignedBookingMap.put(sitterId,bookingIds);
            }
        }
    }

    //Haversine formula
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double radius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    //priority queue
    @Async
    public Queue<SitterPriorityDTO> getPriorityQueue(List<SitterProfile> listSitter, Booking booking){
        int km = 5;
        double average = 2.5;
        int maxRating =5;
        int cancelCount = 4;
        listSitter = listSitter.stream()
                .filter(x->!ValidateUtil.isNullOrEmpty(x.getAccount().getDeviceId()))
                .collect(Collectors.toList());

        Comparator<SitterPriorityDTO> priorityComparator = new Comparator<SitterPriorityDTO>() {
            public int compare(SitterPriorityDTO item1, SitterPriorityDTO item2) {
                return Double.compare(item1.getPriority(), item2.getPriority());
            }
        };

        Queue<SitterPriorityDTO> queue = new PriorityQueue<>(priorityComparator);

        for (SitterProfile sitter:listSitter) {
            double priority = 0;
            //distance
            double latBooking =ValidateUtil.isNullOrEmpty(booking.getLatitude())?0:booking.getLatitude();
            double lonBooking =ValidateUtil.isNullOrEmpty(booking.getLongitude())?0:booking.getLongitude();
            double latSitter=ValidateUtil.isNullOrEmpty(sitter.getLatitude())?0:sitter.getLatitude();
            double lonSitter=ValidateUtil.isNullOrEmpty(sitter.getLongitude())?0:sitter.getLongitude();
            double distance = distance(latBooking,lonBooking,latSitter,lonSitter);
            if(distance < km){
                priority++;
            }
            //rating
            if(sitter.getRate()<=maxRating && sitter.getRate()>=average){
                priority++;
            }else {
                priority-=0.5;
            }
            //cancelcount
            int numOfSitCancel = ValidateUtil.isNullOrEmpty(sitter.getNumberOfCancels())?0:sitter.getNumberOfCancels();
            if(numOfSitCancel<=cancelCount){
                priority++;
            }else {
                priority-=0.5;
            }
            queue.add(new SitterPriorityDTO(sitter.getId(),priority,3));
        }
        return queue;
    }

    private boolean assignSitterDirectly(Queue<SitterPriorityDTO> sitterPriority, Booking booking){
        boolean isSuccess =false;
        for (SitterPriorityDTO sitterPriorityDTO:sitterPriority){
            try {
//                addBookingIdToWaitingListSitter(sitterPriorityDTO.getSitterId(), booking.getId());
                addSitterToBooking(booking.getId(),sitterPriorityDTO.getSitterId());
                isSuccess = true;
                break;
            }
            catch (Exception e){
                removeBookingIdToWaitingListSitter(sitterPriorityDTO.getSitterId(), booking.getId());
                continue;
            }
        }
        return isSuccess;
    }

    private Integer getSlotBaseDate(LocalDateTime dateStart) {

        for (Map.Entry<Integer, SlotDTOV2> map : slotSystem.entrySet()) {
            if (isTimeBetweenTwoTime(dateStart.toLocalTime(), map.getValue().getStartTime(), map.getValue().getEndTime())) {
                return map.getKey();
            }
        }
        return 0;
    }

    private boolean isTimeBetweenTwoTime(LocalTime dateTime, LocalTime starSlot, LocalTime endSlot) {
        return (dateTime.isAfter(starSlot) || dateTime.equals(starSlot)) && dateTime.isBefore(endSlot);
    }
}
