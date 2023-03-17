package com.cpm.account.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "account")
public class AccountEntity {
    @Id
    @Column(name = "id_pk")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence_generator")
    @SequenceGenerator(name = "account_sequence_generator", sequenceName = "account_sequence")
    private Long id;
    @Column(name = "user_id_fk", nullable = false)
    private Long userId;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "closed")
    private LocalDateTime closed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_fk", insertable = false, updatable = false)
    private UserEntity user;
    @OneToMany(mappedBy = "accountId", fetch = FetchType.LAZY)
    private Set<AccountTransactionEntity> transactions= new HashSet<>();
}
