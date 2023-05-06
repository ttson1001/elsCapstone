package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class FeedBack {
    @Id
    @Column(name = "booking_id")
    private String id;

    private Float rate;
    @Column(name = "hash_tag")
    private String hashTag;

    private String comment;

    @MapsId
    @JoinColumn(name = "booking_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @JsonIgnore
    @JoinColumn(name = "sitter_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SitterProfile sitter;
}
