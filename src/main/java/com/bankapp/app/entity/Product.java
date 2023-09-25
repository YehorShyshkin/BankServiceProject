package com.bankapp.app.entity;

import com.bankapp.app.enums.CurrencyCode;
import com.bankapp.app.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id; // Уникальный идентификатор продукта (обычно UUID).

    @Column(name = "name")
    private String name; // Название продукта.

    @Column(name = "product_status")
    private ProductStatus status; // Статус продукта (используется перечисление ProductStatus).

    @Column(name = "currency_code")
    private CurrencyCode currencyCode; // Код валюты продукта (используется перечисление CurrencyCode).

    @Column(name = "interest_rate")
    private BigDecimal interestRate; // Процентная ставка, связанная с продуктом.

    @Column(name = "product_limit")
    private BigDecimal productLimit; // Лимит, связанный с продуктом.

    @Column(name = "created_at")
    private Timestamp createdAt; // Временная метка создания продукта.

    @Column(name = "updated_at")
    private Timestamp updatedAt; // Временная метка обновления продукта.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private List<Agreement> agreements;

    public Product(UUID id, String name, ProductStatus status,
                   CurrencyCode currencyCode, BigDecimal interestRate,
                   BigDecimal productLimit, Timestamp createdAt,
                   Timestamp updatedAt, Manager manager,
                   List<Agreement> agreements) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.currencyCode = currencyCode;
        this.interestRate = interestRate;
        this.productLimit = productLimit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.manager = manager;
        this.agreements = agreements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", currencyCode=" + currencyCode +
                ", interestRate=" + interestRate +
                ", productLimit=" + productLimit +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", manager=" + manager +
                ", agreements=" + agreements +
                '}';
    }
}
