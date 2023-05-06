package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Certificate {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(length = 150, nullable = false)
    private String title;
    @Column(length = 150, nullable = false)
    private String organization;

    @Column(nullable = false)
    private LocalDate dateReceived;
    @Column(length = 100, nullable = false)
    private String idNumber;
    private String url;
    @Column(nullable = false)
    private String imgUrl;
    @Column(length = 20, nullable = false)
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sitter_id", nullable = false)
    private SitterProfile sitterProfile;

}
