package com.bankapp.app.entity;

import com.bankapp.app.enums.ClientStatus;
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
@Table(name = "clients")
@NoArgsConstructor
@Getter
@Setter
/**
 * Клиенты - это физические лица или юридические лица,
 * которые имеют отношения с банком и пользуются его услугами.
 * Клиенты могут выполнять различные операции и иметь разные виды банковских счетов
 * и продуктов.
 */
public class Client  {

    /**
     * Идентификации уникальной записи или объекта в базе данных
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * Это атрибут, который обычно описывает статус или состояние клиента
     * в банковском приложении или системе.
     */
    @Column(name = "client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    /**
     * Это уникальный идентификационный номер,
     * который присваивается физическим и юридическим лицам в целях налогового учета
     */
    @Column(name = "tax_code")
    private String taxCode;

    /**
     * Поле, обозначающее имя клиента (физического лица) в банковском приложении
     * или информационной системе банка.
     */
    @Column(name = "first_name")
    private String clientFirstName;


    /**
     * Поле, обозначающее фамилию клиента (физического лица) в банковском приложении
     * или информационной системе банка.
     */
    @Column(name = "last_name")
    private String clientLastName;


    /**
     * Электронная почта часто связана с аккаунтом клиента и может использоваться
     * для уведомлений, обмена информацией о транзакциях,
     * активности счета и других операциях.
     */
    @Column(name = "email")
    private String email;

    /**
     * Информация об адресе места проживания клиента
     */
    @Column(name = "address")
    private String address;

    /**
     * Информация о номере телефона клиента может быть собрана для связи с клиентом,
     * отправки SMS-уведомлений, аутентификации или как один из способов связи
     * с клиентом.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Дата и время создания клиента.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * Дата и временя последнего обновления записи в базе данных.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;


    /**
     * Представляют собой счета клиентов, на которых хранятся и управляются денежными средствами.
     */
    @OneToMany(mappedBy = "client")
    private List<Account> accounts;

    /**
     * Использоваться для обозначения сотрудника банка,
     * который ответственен за управление и обслуживание клиентов.
     */
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
