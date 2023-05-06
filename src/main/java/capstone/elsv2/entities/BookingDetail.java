package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookingDetail {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(nullable = false)
    private Integer estimateTime;
    @Column(nullable = false, length = 150)
    private String packageName;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private BigDecimal price;
//    @Column( nullable = false)
    private String location;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(nullable = false)
    private BigDecimal percentChange;


    @JsonIgnore
    @OneToMany(mappedBy = "bookingDetail")
    private List<Report> report;

    // foreign key
    @JsonIgnore
    @JoinColumn(name = "package_id",nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Package _package;

    @JsonIgnore
    @OneToMany(mappedBy = "bookingDetail")
    private List<Tracking> trackingList;

    @JsonIgnore
    @JoinColumn(name = "booking_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @JsonIgnore
    @OneToMany(mappedBy = "bookingDetail",fetch = FetchType.LAZY,cascade= CascadeType.ALL)
    private List<DetailService> detailServices;

    public BookingDetail(LocalDateTime startDateTime,LocalDateTime endDateTime){
        this.startDateTime=startDateTime;
        this.endDateTime=endDateTime;
    }
}
