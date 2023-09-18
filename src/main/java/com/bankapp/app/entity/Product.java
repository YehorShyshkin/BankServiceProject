package com.bankapp.app.entity;

import com.bankapp.app.enums.CurrencyCode;
import com.bankapp.app.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Product {
    private UUID id;
    private UUID manager_id;
    private String name;
    private ProductStatus status;
    private CurrencyCode currency_code;
    private int interest_rate;
    private Integer product_limit;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Product(UUID id, UUID manager_id, String name,
                   ProductStatus status, CurrencyCode currency_code,
                   int interest_rate, Integer product_limit,
                   Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.manager_id = manager_id;
        this.name = name;
        this.status = status;
        this.currency_code = currency_code;
        this.interest_rate = interest_rate;
        this.product_limit = product_limit;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id) && Objects.equals(manager_id, product.manager_id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manager_id, name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", manager_id=" + manager_id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", currency_code=" + currency_code +
                ", interest_rate=" + interest_rate +
                ", product_limit=" + product_limit +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
