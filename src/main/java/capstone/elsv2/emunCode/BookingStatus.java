package capstone.elsv2.emunCode;

public enum BookingStatus {
    //unused status
    ACTIVATE,// đang trong trạng thấy làm việc
    CUSTOMER_CANCEL, // customer hủy
    SITTER_CANCEL, // sitter hủy
    //used status
    PENDING, // chờ assign sitter
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED, // làm xong hết viêc
    PAID, // đã thanh toán booking
    CANCEL, // hủy booking
}
