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
@Setter
@Builder
public class Elder {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "healthStatus", nullable = false, length = 100)
    private String healthStatus;
    @Column(name = "id_card_number", unique = true, length = 12, nullable = false)
    private String idCardNumber;
    @Column(length = 1000)
    private String note;
    @Column(nullable = false, length = 20)
    private String status;

    @Column()
    private String gender;


    //foreign key
    @JsonIgnore
    @OneToMany(mappedBy = "elder")
    private List<Relationship> relationships;
    @JsonIgnore
    @OneToMany(mappedBy = "elder")
    private List<Booking> bookings;

}
