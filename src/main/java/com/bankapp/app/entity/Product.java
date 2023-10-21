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
    private String productName; // Название продукта.

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus; // Статус продукта (используется перечисление ProductStatus).

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode; // Код валюты продукта (используется перечисление CurrencyCode).

    @Column(name = "interest_rate")
    private BigDecimal productInterestRate; // Процентная ставка, связанная с продуктом.

    @Column(name = "product_limit")
    private BigDecimal productLimit; // Лимит, связанный с продуктом.

    @Column(name = "created_at")
    private Timestamp createdAt; // Временная метка создания продукта.

    @Column(name = "updated_at")
    private Timestamp updatedAt; // Временная метка обновления продукта.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    @OneToMany(mappedBy = "product")
    private List<Agreement> agreements;

    public Product(UUID id, String productName, ProductStatus productStatus,
                   CurrencyCode currencyCode, BigDecimal productInterestRate,
                   BigDecimal productLimit, Timestamp createdAt,
                   Timestamp updatedAt, Manager manager,
                   List<Agreement> agreements) {
        this.id = id;
        this.productName = productName;
        this.productStatus = productStatus;
        this.currencyCode = currencyCode;
        this.productInterestRate = productInterestRate;
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
        return Objects.equals(productName, product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + productName + '\'' +
                ", status=" + productStatus +
                ", currencyCode=" + currencyCode +
                ", interestRate=" + productInterestRate +
                ", productLimit=" + productLimit +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", manager=" + manager +
                ", agreements=" + agreements +
                '}';
    }
}
