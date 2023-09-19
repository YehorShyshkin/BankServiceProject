package com.bankapp.app.entity;

import com.bankapp.app.enums.CurrencyCode;
import com.bankapp.app.enums.ProductStatus;
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
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id

    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id; // Представляет уникальный идентификатор продукта, обычно это UUID.

    @Column(name = "name")
    private String name; // Содержит название продукта.

    @Column(name = "product_status")
    private ProductStatus status; // Представляет статус продукта, который имеет тип ProductStatus

    @Column(name = "currency_code")
    private CurrencyCode currencyCode; // Хранит код валюты продукта, который имеет тип CurrencyCode.

    @Column(name = "interest_rate")
    private double interestRate; // Содержит процентную ставку, связанную с продуктом.

    @Column(name = "product_limit")
    private int productLimit; // Представляет лимит, связанный с продуктом.

    @Column(name = "created_at")
    private Timestamp createdAt; // Содержат временные метки, указывающие на момент создания продукта.

    @Column(name = "updated_at")
    private Timestamp updatedAt; //Содержат временные метки, указывающие на момент обновления продукта.

    /**
     * Поле manager связано с аннотацией @OneToMany,
     * что указывает на то, что один продукт может иметь одного
     * менеджера (Manager).
     */
    @Column(name = "manager")
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY,
    orphanRemoval = true,cascade = {MERGE,PERSIST,REFRESH})
    private List<Manager> manager;


    /**
     * Поле agreements также связано с аннотацией @OneToMany, что указывает на то,
     * что один продукт может иметь множество соглашений (Agreement).
     */
    @Column(name = "agreements")
    @OneToMany(mappedBy = "agreements", fetch = FetchType.LAZY,
    orphanRemoval = true,cascade = {MERGE,PERSIST,REFRESH})
    private List<Agreement> agreements;

    public Product(UUID id, String name, ProductStatus status, CurrencyCode currencyCode, double interestRate, int productLimit, Timestamp createdAt,
                   Timestamp updatedAt, List<Manager> manager, List<Agreement> agreements) {
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
