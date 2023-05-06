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
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    @Column(length = 64, nullable = true)
    private String password;

    @Column(length = 10, unique = true, nullable = false)
    private String phone;
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(length = 20, nullable = false)
    private String status;
    @Column(name = "device_id")
    private String deviceId;
    @Column(length = 4)
    private String otp;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private CustomerProfile customerProfile;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private SitterProfile sitterProfile;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private StaffProfile staffProfile;

    @JsonIgnore
    @JoinColumn(name = "role_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "account")
    private Wallet wallet;
}
