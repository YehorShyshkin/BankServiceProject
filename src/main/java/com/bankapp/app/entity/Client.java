package com.bankapp.app.entity;

import com.bankapp.app.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Client {
    private UUID id;
    private int managerId;
    //private Manager manager;
    private ClientStatus status;
    private Integer taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Client(UUID id, ClientStatus status, Integer taxCode,
                  String firstName, String lastName,
                  String email, String address, String phone,
                  Timestamp createdAt, Timestamp updatedAt, int managerId) {
        this.id = id;
        this.status = status;
        this.taxCode = taxCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.managerId = managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(id, client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(phone, client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", status=" + status +
                ", tax_code=" + taxCode +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                ", manager_id=" + managerId +
                '}';
    }
}
