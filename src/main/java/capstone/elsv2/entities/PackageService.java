package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "packageService", columnNames = {"package_id", "service_id"})})
public class PackageService {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @JsonIgnore
    @JoinColumn(name = "package_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Package _package;

    @JsonIgnore
    @JoinColumn(name = "service_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Service service;
    @Column(nullable = false, length = 20)
    private String status;
}
