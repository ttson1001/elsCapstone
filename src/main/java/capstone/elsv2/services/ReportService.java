package capstone.elsv2.services;

import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.report.AddReportDTO;
import capstone.elsv2.dto.report.ReportDTO;
import capstone.elsv2.dto.report.ReportDetailDTO;
import capstone.elsv2.dto.report.ReportReplyDTO;

import java.util.List;

public interface ReportService {
    Boolean addReportCustomerToSitter(AddReportDTO reportDTO);

    Boolean addReportSitterToCustomer(AddReportDTO reportDTO);

    Boolean replyReport(ReportReplyDTO reportReplyDTO);

    List<ReportDTO> getAllReportForCustomer(String customerId);

    List<ReportDTO> getAllReportForSitter(String customerId);

    PageDTO getAllFormReport(int pageNumber, int pageSize);

    ReportDetailDTO getReportDetail(String id);

}
