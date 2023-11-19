package com.bankapp.app.entity;

import com.bankapp.app.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@ToString
@Getter
@Setter
/**
 * ----- Russian ------
 * Клиенты - это физические лица или юридические лица,
 * которые имеют отношения с банком и пользуются его услугами.
 * Клиенты могут выполнять различные операции и иметь разные виды банковских счетов
 * и продуктов.
 * <p>
 * ----- English -------
 * Clients are individuals or legal entities
 * who have relationships with the bank and use its services.
 * Clients can perform various operations and have different types of bank accounts and products.
 */
public class Client {

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
    private UUID id;

    /**
     * ----- Russian ------
     * Это атрибут, который обычно описывает статус или состояние клиента
     * в банковском приложении или системе.
     * <p>
     * ----- English -------
     * An attribute that usually describes the status or state of the client
     * in the bank application or system.
     */
    @Column(name = "client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    /**
     * ----- Russian ------
     * Это уникальный идентификационный номер,
     * который присваивается физическим и юридическим лицам в целях налогового учета.
     * <p>
     * ----- English -------
     * This is a unique identification number
     * assigned to individuals and legal entities for tax purposes.
     */
    @Column(name = "tax_code")
    private String taxCode;

    /**
     * ----- Russian ------
     * Поле, обозначающее имя клиента (физического лица) в банковском приложении
     * или информационной системе банка.
     * <p>
     * ----- English -------
     * Field denoting the first name of the client (individual) in the bank application
     * or the bank's information system.
     */
    @Column(name = "first_name")
    private String clientFirstName;


    /**
     * ----- Russian ------
     * Поле, обозначающее фамилию клиента (физического лица) в банковском приложении
     * или информационной системе банка.
     * <p>
     * ----- English -------
     * Field denoting the last name of the client (individual) in the bank application
     * or the bank's information system.
     */
    @Column(name = "last_name")
    private String clientLastName;


    /**
     * ----- Russian ------
     * Электронная почта часто связана с аккаунтом клиента и может использоваться
     * для уведомлений, обмена информацией о транзакциях,
     * активности счета и других операциях.
     * <p>
     * ----- English -------
     * Email is often associated with the client's account and can be used
     * for notifications, exchanging transaction information,
     * account activity, and other operations.
     */
    @Column(name = "email")
    private String email;

    /**
     * ----- Russian ------
     * Информация об адресе места проживания клиента
     * <p>
     * ----- English -------
     * Information about the client's place of residence.
     */
    @Column(name = "address")
    private String address;

    /**
     * ----- Russian ------
     * Информация о номере телефона клиента может быть собрана для связи с клиентом,
     * отправки SMS-уведомлений, аутентификации или как один из способов связи
     * с клиентом.
     * <p>
     * ----- English -------
     * Information about the client's phone number can be collected
     * for communication with the client, sending SMS notifications,
     * authentication, or as one of the means of communication with the client.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * ----- Russian ------
     * Дата и время создания клиента.
     * <p>
     * ----- English -------
     * Date and time of client creation.
     */
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * ----- Russian ------
     * Дата и временя последнего обновления записи в базе данных.
     * <p>
     * ----- English -------
     * Date and time of the last update to the database record.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;


    /**
     * ----- Russian ------
     * Представляют собой счета клиентов, на которых хранятся и управляются денежными средствами.
     * <p>
     * ----- English -------
     * Represents the client's accounts where money is stored and managed.
     */
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Account> accounts;

    /**
     * ----- Russian ------
     * Использоваться для обозначения сотрудника банка,
     * который ответственен за управление и обслуживание клиентов.
     * <p>
     * ----- English -------
     * Used to denote a bank employee responsible for managing and serving clients.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public Client(UUID id, ClientStatus clientStatus,
                  String taxCode, String clientFirstName,
                  String clientLastName, String email,
                  String address, String phoneNumber,
                  Timestamp createdAt, Timestamp updatedAt,
                  List<Account> accounts, Manager manager) {
        this.id = id;
        this.clientStatus = clientStatus;
        this.taxCode = taxCode;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
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

}
