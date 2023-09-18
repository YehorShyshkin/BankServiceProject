package com.bankapp.app.entity;

import com.bankapp.app.enums.ManagerStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Manager {
    private UUID id;
    private String firstName;
    private String last_name;
    private ManagerStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Manager(UUID id, String firstName, String last_name,
                   ManagerStatus status, Timestamp createdAt,
                   Timestamp updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.last_name = last_name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager manager)) return false;
        return Objects.equals(id, manager.id) && Objects.equals(firstName, manager.firstName) && Objects.equals(last_name, manager.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, last_name);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + last_name + '\'' +
                ", status=" + status +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }
}
