package com.bankapp.app.entity;

import com.bankapp.app.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "first_name")
    private String clientFirstName;

    @Column(name = "last_name")
    private String clientLastName;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public Client(UUID id, ClientStatus clientStatus, String taxCode, String clientFirstName,
                  String clientLastName, String email, String address, String phone,
                  Timestamp createdAt, Timestamp updatedAt,
                  List<Account> accounts, Manager manager) {
        this.id = id;
        this.clientStatus = clientStatus;
        this.taxCode = taxCode;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.accounts = accounts;
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(taxCode, client.taxCode) && Objects.equals(phoneNumber, client.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxCode, phoneNumber);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", status=" + clientStatus +
                ", taxCode=" + taxCode +
                ", firstName='" + clientFirstName + '\'' +
                ", lastName='" + clientLastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", accounts=" + accounts +
                ", manager=" + manager +
                '}';
    }
}
