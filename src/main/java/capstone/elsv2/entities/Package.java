package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Package {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private Integer startSlot;
    @Column(nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    private Integer endSlot;

    @Column(name = "health_status", length = 100, nullable = false)
    private String healthStatus;
    @Column(length = 1000)
    private String description;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    // foreign key

    @OneToMany(mappedBy = "_package")
    @JsonIgnore
    private List<PackageService> packageServices;

    @JsonIgnore
    @OneToMany(mappedBy = "_package")
    private List<BookingDetail> bookingDetails;
    @JsonIgnore
    @OneToMany(mappedBy = "_package")
    private List<SitterPackage> sitterPackages;


}
