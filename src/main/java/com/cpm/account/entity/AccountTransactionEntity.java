package com.cpm.account.entity;

import com.cpm.account.statics.TransactionType;
import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account_transaction")
public class AccountTransactionEntity {
    @Id
    @Column(name = "id_pk")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_transaction_sequence_generator")
    @SequenceGenerator(name = "account_transaction_sequence_generator", sequenceName = "account_transaction_sequence")
    private Long id;
    @Column(name = "account_id_fk")
    private Long accountId;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id_fk", insertable = false, updatable = false)
    private AccountEntity account;
}
