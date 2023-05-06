package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Report {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
//    private String image;

    @Column(name = "create_date",nullable = false)
    private LocalDateTime createDate;
//    @Column(nullable = false)
    private String reply;
    @Column(nullable = false, length = 20)
    private String status;

    @JoinColumn(name = "booking_detail_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BookingDetail bookingDetail;

    @JsonIgnore
    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerProfile customer;

    @JsonIgnore
    @JoinColumn(name = "sitter_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SitterProfile sitter;


}
