package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Service {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false)
    private Float duration;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, length = 20)
    private String status;

//    @Column(name = "img_url")
//    private String imgUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<PackageService> packageServices;
}
