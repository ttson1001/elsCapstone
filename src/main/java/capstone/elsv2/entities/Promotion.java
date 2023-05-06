package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Promotion {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private Float value;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date",nullable = false)
    private LocalDate endDate;
    @Column(nullable = false, unique = true,length = 16)
    private String code;
    @Column(nullable = false, length = 20)
    private String status;
    @OneToMany(mappedBy = "promotion")
    @JsonIgnore
    private List<Booking> bookings;
}
