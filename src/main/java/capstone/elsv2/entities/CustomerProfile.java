package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerProfile {
    @Id
    @Column(name = "account_id")
    private String id;
    @Column(length = 5)
    private String gender;

    private String address;

    @Column(name = "avatar_img_url", nullable = true)
    private String avatarImgUrl;

    @Column(nullable = true)
    private LocalDate dob;
    @Column(name = "number_of_cancels")
    private Integer numberOfCancels;

    @Column(length = 1000)
    private String description;
    @Column(name = "id_card_number", unique = true)
    private String idCardNumber;
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "account_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;
    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Relationship> relationships;


}
