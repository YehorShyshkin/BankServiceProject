package com.bankapp.app.entity;

import com.bankapp.app.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "managers")
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
    private String lastName;

    @Column(name = "manager_status")
    @Enumerated(EnumType.STRING)
    private ManagerStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "manager")
    private List<Client> clients;

    @OneToMany(mappedBy = "manager")
    private List<Product> products;

    public Manager(UUID id, String firstName, String last_name, ManagerStatus status, Timestamp createdAt, Timestamp updatedAt, List<Client> clients, List<Product> products) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = last_name;
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
        return Objects.equals(firstName, manager.firstName) && Objects.equals(lastName, manager.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", clients=" + clients +
                ", products=" + products +
                '}';
    }
}
