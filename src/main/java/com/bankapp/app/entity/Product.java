package com.bankapp.app.entity;

import com.bankapp.app.enums.CurrencyCode;
import com.bankapp.app.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    /**
     * ---- Russian -------
     * <p>
     * Идентификации уникальной записи или объекта в базе данных.
     * <p>
     * ----- English -------
     * <p>
     * Unique identifier of the record or object in the database.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * ---- Russian -------
     * <p>
     * Название продукта.
     * <p>
     * ----- English -------
     * <p>
     * Name of the product.
     */
    @Column(name = "name")
    private String productName;

    /**
     * ---- Russian -------
     * <p>
     * Статус продукта (используется перечисление ProductStatus).
     * <p>
     * ----- English -------
     * <p>
     * Status of the product (using the enumeration ProductStatus).
     */
    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    /**
     * ---- Russian -------
     * <p>
     * Код валюты продукта (используется перечисление CurrencyCode).
     * <p>
     * ----- English -------
     * <p>
     * Currency code of the product (using the enumeration CurrencyCode).
     */
    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    /**
     * ---- Russian -------
     * <p>
     * Процентная ставка, связанная с продуктом.
     * <p>
     * ----- English -------
     * <p>
     * Interest rate associated with the product.
     */
    @Column(name = "interest_rate")
    private BigDecimal productInterestRate;

    /**
     * ---- Russian -------
     * <p>
     * Лимит, связанный с продуктом.
     * <p>
     * ----- English -------
     * <p>
     * Limit associated with the product.
     */
    @Column(name = "product_limit")
    private BigDecimal productLimit;

    /**
     * ---- Russian -------
     * <p>
     * Дата и время создания продукта.
     * <p>
     * ----- English -------
     * <p>
     * Date and time of product creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ---- Russian -------
     * <p>
     * Дата и время обновления продукта.
     * <p>
     * ----- English -------
     * <p>
     * Date and time of product update.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ---- Russian -------
     * <p>
     * Ссылка на ответственного менеджера.
     * <p>
     * ----- English -------
     * <p>
     * Reference to the responsible manager.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    /**
     * ---- Russian -------
     * <p>
     * Ссылка на лист банковских соглашений.
     * <p>
     * ----- English -------
     * <p>
     * Reference to the list of banking agreements.
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
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
