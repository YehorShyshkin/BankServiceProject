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
    private String managerFirstName;

    @Column(name = "last_name")
    private String managerLastName;

    @Column(name = "manager_status")
    @Enumerated(EnumType.STRING)
    private ManagerStatus managerStatus;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "manager")
    private List<Client> listClients;

    @OneToMany(mappedBy = "manager")
    private List<Product> listProducts;

    public Manager(UUID id, String managerFirstName, String last_name, ManagerStatus managerStatus, Timestamp createdAt, Timestamp updatedAt, List<Client> listClients, List<Product> listProducts) {
        this.id = id;
        this.managerFirstName = managerFirstName;
        this.managerLastName = last_name;
        this.managerStatus = managerStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.listClients = listClients;
        this.listProducts = listProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager manager)) return false;
        return Objects.equals(managerFirstName, manager.managerFirstName) && Objects.equals(managerLastName, manager.managerLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managerFirstName, managerLastName);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", firstName='" + managerFirstName + '\'' +
                ", last_name='" + managerLastName + '\'' +
                ", status=" + managerStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", clients=" + listClients +
                ", products=" + listProducts +
                '}';
    }
}
