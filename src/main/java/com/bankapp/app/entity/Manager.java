package com.bankapp.app.entity;

import com.bankapp.app.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "managers")
@NoArgsConstructor
@Getter
@Setter

/**
 * ----- Russian ------
 * Этот класс представляет собой менеджера в банковской системе.
 * <p>
 *  ----- English -------
 * This class represents a manager in the banking system.
 */
public class Manager {

    /**
     * ----- Russian ------
     * Идентификации уникальной записи или объекта в базе данных.
     * <p>
     * ----- English -------
     * Unique identifier of the record or object in the database.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @SequenceGenerator(name = "manager_entity_generator", sequenceName = "manager_entyti_seq", allocationSize = 1)
    private UUID id;

    /**
     * ----- Russian ------
     * Имя менеджера.
     * <p>
     * ----- English -------
     * First name of the manager
     */

    @Column(name = "first_name")
    private String managerFirstName;

    /**
     * ----- Russian ------
     * Фамилия менеджера.
     * <p>
     * ----- English -------
     * Last name of the manager
     */
    @Column(name = "last_name")
    private String managerLastName;

    /**
     * ----- Russian ------
     * Статус менеджера (например, активен, неактивен и т. д.).
     * <p>
     * ----- English -------
     * Status of the manager (e.g., active, inactive, etc.).
     */
    @Column(name = "manager_status")
    @Enumerated(EnumType.STRING)
    private ManagerStatus managerStatus;

    /**
     * ----- Russian ------
     * Дата и время создания записи о менеджере.
     * <p>
     * ----- English -------
     * Date and time when the manager record was created.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ----- Russian ------
     * Дата и время последнего обновления записи о менеджере.
     * <p>
     * ----- English -------
     * Date and time of the last update to the manager record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    /**
     * ----- Russian ------
     * Список клиентов, связанных с данным менеджером.
     * <p>
     * ----- English -------
     * List of clients associated with this manager.
     */

    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
    private List<Client> listClients;

    /**
     * ----- Russian ------
     * Список продуктов, связанных с данным менеджером.
     * <p>
     * ----- English -------
     * List of products associated with this manager.
     */

    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
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
        return Objects.equals(id, manager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
