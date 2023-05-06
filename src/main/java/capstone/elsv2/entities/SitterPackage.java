package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "sitterPackage", columnNames = {"sitter_id", "package_id"})})
public class SitterPackage {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sitter_id", nullable = false)
    private SitterProfile sitterProfile;

    @JsonIgnore
    @JoinColumn(name = "package_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Package _package;
    @Column(nullable = false, length = 20)
    private String status;
}
