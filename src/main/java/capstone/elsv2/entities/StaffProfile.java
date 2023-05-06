package capstone.elsv2.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StaffProfile {
    @Id
    @Column(name = "account_id")
    private String id;
    @Column(length = 5, nullable = false)
    private String gender;

    @Column(name = "avtar_img")
    private String avatarImg;

    @Column(length = 150)
    private String address;
    @Column(nullable = false)
    private LocalDate dob;
    @MapsId
    @JoinColumn(name = "account_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

}
