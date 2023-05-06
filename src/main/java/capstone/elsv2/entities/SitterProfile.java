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
public class SitterProfile {
    @Id
    @Column(name = "account_id")
    private String id;
    @Column(name = "avatar_img_url")
    private String avatarImgUrl;
    @Column(length = 5)
    private String gender;
    @Column(length = 150)
    private String address;
    private Float rate;
    private LocalDate dob;
    @Column(length = 1000)
    private String description;
    @Column(name = "id_card_number", length = 12, unique = true)
    private String idCardNumber;
    @Column(name = "back_card_img_url")
    private String backCardImgUrl;

    @Column(name = "front_card_img_url")
    private String frontCardImgUrl;
    @Column(name = "number_of_cancels")
    private Integer numberOfCancels;

    private Double latitude;
    private Double longitude;

    @Column(length = 1000)
    private String reason;//
    @MapsId
    @JoinColumn(name = "account_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "sitterProfile")
    private List<Certificate> certificateSitters;


    @JsonIgnore
    @OneToMany(mappedBy = "sitter")
    private List<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "sitter")
    private List<WorkExperience> workExperiences;

    @JsonIgnore
    @OneToMany(mappedBy = "sitter")
    private List<WorkingTime> workingTimes;

    @JsonIgnore
    @JoinColumn(name = "zone_zone_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Zone zone;
}
