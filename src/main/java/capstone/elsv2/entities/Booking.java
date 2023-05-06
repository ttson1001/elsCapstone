package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Booking {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(length = 150, nullable = false)
    private String address;
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 10000)
    private String slots;
    @Column(nullable = false)
    private BigDecimal deposit;

    private String reason;
    private LocalDate cancelDate;

    @Column(length = 1000, nullable = false)
    private String description;
    // healstatus

    @Column(length = 20, nullable = false)
    private String status;

    @Column(name="number_of_change")
    private Integer numOfChange;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    private Double latitude;

    private Double Longitude;
    private String district;

    // foreign key
    @JsonIgnore
    @JoinColumn(name = "elder_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Elder elder;

    @JsonIgnore
    @JoinColumn(name = "sitter_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SitterProfile sitter;

    @JsonIgnore
    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerProfile customer;

    @JsonIgnore
    @OneToMany(mappedBy = "booking")
    private List<Transaction> walletTransactionList;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails;

    @JsonIgnore
    @JoinColumn(name = "promotion_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Promotion promotion;

    @JsonIgnore
    @OneToOne(mappedBy = "booking")
    private FeedBack rating;

}
