package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.customer.CustomerDTO;
import capstone.elsv2.dto.report.*;
import capstone.elsv2.dto.sitter.SitterDTO;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.BookingDetail;
import capstone.elsv2.entities.CustomerProfile;
import capstone.elsv2.entities.Report;
import capstone.elsv2.entities.SitterProfile;
import capstone.elsv2.repositories.BookingDetailRepository;
import capstone.elsv2.repositories.CustomerProfileRepository;
import capstone.elsv2.repositories.ReportRepository;
import capstone.elsv2.repositories.SitterProfileRepository;
import capstone.elsv2.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private SitterProfileRepository sitterProfileRepository;
    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    private final ZoneId istZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    @Override
    public Boolean addReportCustomerToSitter(AddReportDTO reportDTO) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(reportDTO.getBookingDetailId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy booking deatil nào"));
        SitterProfile sitterProfile = sitterProfileRepository.findById(reportDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy chăm sóc viên nào"));
        CustomerProfile customerProfile = customerProfileRepository.findById(reportDTO.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy khách hàng nào"));
        try {
            Report report = Report.builder()
                    .title(reportDTO.getTitle())
                    .status(StatusCode.CUSTOMER_PENDING.toString())
                    .content(reportDTO.getContent())
                    .sitter(sitterProfile)
//                    .image(reportDTO.getImage())
                    .createDate(LocalDateTime.now(istZoneId))
                    .customer(customerProfile)
                    .bookingDetail(bookingDetail)
                    .build();
            reportRepository.save(report);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(400), "không thể tạo báo cáo");
        }
        return true;
    }

    @Override
    public Boolean addReportSitterToCustomer(AddReportDTO reportDTO) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(reportDTO.getBookingDetailId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy booking deatil nào"));
        SitterProfile sitterProfile = sitterProfileRepository.findById(reportDTO.getSitterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy chăm sóc viên nào"));
        CustomerProfile customerProfile = customerProfileRepository.findById(reportDTO.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "không tìm thấy khách hàng nào"));
        try {
            Report report = Report.builder()
                    .title(reportDTO.getTitle())
                    .status(StatusCode.SITTER_PENDING.toString())
                    .content(reportDTO.getContent())
                    .sitter(sitterProfile)
                    .createDate(LocalDateTime.now())
                    .customer(customerProfile)
                    .bookingDetail(bookingDetail)
                    .build();
            reportRepository.save(report);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "không thể tạo báo cáo");
        }
        return true;
    }

    @Override
    public Boolean replyReport(ReportReplyDTO reportReplyDTO) {
        Report report = reportRepository.findById(reportReplyDTO.getReportId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy được báo cáo nào"));

        if (report.getStatus().equals(StatusCode.CUSTOMER_PENDING.toString())) {
            report.setStatus(StatusCode.REPLIED_CUSTOMER.toString());
            report.setReply(reportReplyDTO.getReply());
            reportRepository.save(report);
        } else if(report.getStatus().equals(StatusCode.SITTER_PENDING.toString())){
            report.setStatus(StatusCode.REPLIED_SITTER.toString());
            report.setReply(reportReplyDTO.getReply());
            reportRepository.save(report);
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Báo cáo đã đươc phản hồi rồi");
        }
        return true;
    }

    @Override
    public List<ReportDTO> getAllReportForCustomer(String customerId) {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        List<Report> reports = reportRepository.findAllByCustomer_IdOrderByCreateDateDesc(customerId);
        for (Report report: reports) {
            if(report.getStatus().equals(StatusCode.CUSTOMER_PENDING.toString())||report.getStatus().equals(StatusCode.REPLIED_CUSTOMER.toString())){
                ReportDTO reportDTO = ReportDTO.builder()
                        .id(report.getId())
                        .status(report.getStatus())
                        .title(report.getTitle())
                        .createDate(report.getCreateDate())
                        .reportedPerson(report.getSitter().getAccount().getFullName())
                        .reporter(report.getCustomer().getAccount().getFullName())
                        .build();
                reportDTOS.add(reportDTO);
            }
        }
        return reportDTOS;

    }

    @Override
    public List<ReportDTO> getAllReportForSitter(String sitterId) {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        List<Report> reports = reportRepository.findAllBySitter_IdOrderByCreateDateDesc(sitterId);
        for (Report report: reports) {
            if(report.getStatus().equals(StatusCode.SITTER_PENDING.toString())||report.getStatus().equals(StatusCode.REPLIED_SITTER.toString())){
                ReportDTO reportDTO = ReportDTO.builder()
                        .id(report.getId())
                        .status(report.getStatus())
                        .title(report.getTitle())
                        .createDate(report.getCreateDate())
                        .reporter(report.getSitter().getAccount().getFullName())
                        .reportedPerson(report.getCustomer().getAccount().getFullName())
                        .build();
                reportDTOS.add(reportDTO);
            }
        }
        return reportDTOS;
    }

    @Override
    public PageDTO getAllFormReport(int pageNumber, int pageSize) {
        List<ReportDTO> reportDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Report> reportPage = reportRepository.findAll(pageable);
        List<Report> reports = reportPage.getContent();
        for (Report report: reports) {
            if(report.getStatus().equals(StatusCode.SITTER_PENDING.toString())){
                ReportDTO reportDTO = ReportDTO.builder()
                        .id(report.getId())
                        .reporter(report.getSitter().getAccount().getFullName())
                        .reportedPerson(report.getCustomer().getAccount().getFullName())
                        .status(report.getStatus())
                        .title(report.getTitle())
                        .createDate(report.getCreateDate())
                        .build();
                reportDTOS.add(reportDTO);
            }
            if(report.getStatus().equals(StatusCode.CUSTOMER_PENDING.toString())){
                ReportDTO reportDTO = ReportDTO.builder()
                        .id(report.getId())
                        .reporter(report.getCustomer().getAccount().getFullName())
                        .reportedPerson(report.getSitter().getAccount().getFullName())
                        .status(report.getStatus())
                        .title(report.getTitle())
                        .createDate(report.getCreateDate())
                        .build();
                reportDTOS.add(reportDTO);
            }
        }
        PageDTO pageDTO = PageDTO.builder()
                .data(reportDTOS)
                .pageSize(reportPage.getSize())
                .hasNextPage(reportPage.hasNext())
                .pageIndex(reportPage.getNumber() + 1)
                .hasPreviousPage(reportPage.hasPrevious())
                .totalPages(reportPage.getTotalPages())
                .build();
        return pageDTO;
    }

    @Override
    public ReportDetailDTO getReportDetail(String id) {
        ReportDetailDTO reportDetailDTO ;
        Report report = reportRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy báo cáo nào"));

        if(report.getStatus().equals(StatusCode.CUSTOMER_PENDING.toString())) {
            ReporterDTO reporterDTO = ReporterDTO.builder()
                    .id(report.getCustomer().getId())
                    .status(report.getCustomer().getAccount().getStatus())
                    .email(report.getCustomer().getAccount().getEmail())
                    .gender(report.getCustomer().getGender())
                    .address(report.getCustomer().getAddress())
                    .phone(report.getCustomer().getAccount().getPhone())
                    .role("CUSTOMER")
                    .fullName(report.getCustomer().getAccount().getFullName())
                    .build();
            ReportedPersonDTO reportedPersonDTO = ReportedPersonDTO.builder()
                    .id(report.getSitter().getId())
                    .status(report.getSitter().getAccount().getStatus())
                    .email(report.getSitter().getAccount().getEmail())
                    .gender(report.getSitter().getGender())
                    .address(report.getSitter().getAddress())
                    .phone(report.getSitter().getAccount().getPhone())
                    .role("SITTER")
                    .fullName(report.getSitter().getAccount().getFullName())
                    .build();
            reportDetailDTO = ReportDetailDTO.builder()
                    .reporterDTO(reporterDTO)
                    .id(report.getId())
                    .reportedPersonDTO(reportedPersonDTO)
                    .content(report.getContent())
                    .createDate(report.getCreateDate())
                    .status(report.getStatus())
                    .title(report.getTitle())
                    .build();
        }
        else {
            ReporterDTO reporterDTO = ReporterDTO.builder()
                    .id(report.getSitter().getId())
                    .status(report.getSitter().getAccount().getStatus())
                    .email(report.getSitter().getAccount().getEmail())
                    .gender(report.getSitter().getGender())
                    .address(report.getSitter().getAddress())
                    .phone(report.getSitter().getAccount().getPhone())
                    .role("SITTER")
                    .fullName(report.getSitter().getAccount().getFullName())
                    .build();
            ReportedPersonDTO reportedPersonDTO = ReportedPersonDTO.builder()
                    .id(report.getCustomer().getId())
                    .status(report.getCustomer().getAccount().getStatus())
                    .email(report.getCustomer().getAccount().getEmail())
                    .gender(report.getCustomer().getGender())
                    .address(report.getCustomer().getAddress())
                    .phone(report.getCustomer().getAccount().getPhone())
                    .role("CUSTOMER")
                    .fullName(report.getCustomer().getAccount().getFullName())
                    .build();
            reportDetailDTO = ReportDetailDTO.builder()
                    .reporterDTO(reporterDTO)
                    .id(report.getId())
                    .reportedPersonDTO(reportedPersonDTO)
                    .content(report.getContent())
                    .createDate(report.getCreateDate())
                    .status(report.getStatus())
                    .title(report.getTitle())
                    .build();
        }
        return  reportDetailDTO;
    }

}
