package com.bankapp.app.entity;

import com.bankapp.app.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "manager")
@NoArgsConstructor
@Getter
@Setter
public class Manager {

    @Id
    @GeneratedValue(generator = "UUID")

    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "manager_status")
    private ManagerStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "clients")
    @OneToMany(mappedBy = "clients", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private List<Client> clients;

    @Column(name = "products")
    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
            orphanRemoval = true, cascade = {MERGE, PERSIST, REFRESH})
    private List<Product> products;

    public Manager(UUID id, String firstName, String last_name, ManagerStatus status, Timestamp createdAt, Timestamp updatedAt, List<Client> clients, List<Product> products) {
        this.id = id;
        this.firstName = firstName;
        this.last_name = last_name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.clients = clients;
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager manager)) return false;
        return Objects.equals(firstName, manager.firstName) && Objects.equals(last_name, manager.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, last_name);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", last_name='" + last_name + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", clients=" + clients +
                ", products=" + products +
                '}';
    }
}
