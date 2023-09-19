package com.bankapp.app.entity;

import com.bankapp.app.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "agreement")
@NoArgsConstructor
@Getter
@Setter
public class Agreement {

    @Id
    @GeneratedValue(generator = "UUID")

    @Column(name = "id")
    private UUID id;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "sum")
    private double sum;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "products")
    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
    orphanRemoval = true,cascade = {MERGE,PERSIST,REFRESH})
    private List<Product> products;

    @Column(name = "accounts")
    @OneToMany(mappedBy = "accounts", fetch = FetchType.LAZY,
    orphanRemoval = true, cascade = {MERGE,PERSIST,REFRESH})
    private List<Account> accounts;

    public Agreement(UUID id, double interestRate, AccountStatus status,
                     double sum, Timestamp createdAt, Timestamp updatedAt,
                     List<Product> products, List<Account> accounts) {
        this.id = id;
        this.interestRate = interestRate;
        this.status = status;
        this.sum = sum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.products = products;
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreement agreement)) return false;
        return Double.compare(interestRate, agreement.interestRate) == 0 && status == agreement.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(interestRate, status);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", interestRate=" + interestRate +
                ", status=" + status +
                ", sum=" + sum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", products=" + products +
                ", accounts=" + accounts +
                '}';
    }
}
