package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "education")
@Builder
public class Education {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @Column(name = "education_level", nullable = false, length = 150)
    private String educationLevel;
    @Column(length = 150, nullable = false)
    private String major;
    @Column(name = "school_name", length = 150, nullable = false)
    private String schoolName;
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "is_graduate")
    private Boolean isGraduate;
    private Float GPA;

    @Column(name = "education_Img", nullable = false)
    private String educationImg;
    @Column(length = 20, nullable = false)
    private String status;
    @Column(length = 1000)
    private String description;

    // fk
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sitter_id", nullable = false)
    private SitterProfile sitter;

}
