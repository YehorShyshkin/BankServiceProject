package com.bankapp.app.model;

import com.bankapp.app.model.enums.AccountStatus;
import com.bankapp.app.model.enums.AccountType;
import com.bankapp.app.model.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode code;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Agreement> agreementList;

    @OneToMany(mappedBy = "debitAccount")
    private Set<Transaction> debitTransaction;

    @OneToMany(mappedBy = "creditAccount")
    private Set<Transaction> creditTransaction;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(name, account.name) && status == account.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }

}
