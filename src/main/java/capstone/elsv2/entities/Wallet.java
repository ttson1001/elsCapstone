package capstone.elsv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Wallet {
    @Id
    @Column(name = "account_id",length = 36)
    private String id;
    @Column(nullable = false)
    private BigDecimal amount;

    @MapsId
    @JsonIgnore
    @JoinColumn(name = "account_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    private List<Transaction> walletTransactionList;


}
