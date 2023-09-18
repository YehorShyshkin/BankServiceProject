package com.bankapp.app.enums;

/**
 * тип счета
 */
public enum AccountType {
    CHECKING_ACCOUNT, // Текущий счет предназначен для повседневных финансовых операций. С него можно совершать платежи, писать чеки, снимать деньги и делать переводы. На таких счетах часто не начисляются или начисляются небольшие проценты.
    CERTIFICATE_OF_DEPOSIT, // Депозитный счет - это счет, на который клиент вносит сумму денег на фиксированный срок с фиксированной процентной ставкой. В обмен на это, клиент обязуется не снимать средства с счета до окончания срока. Депозиты обычно предоставляют более высокие проценты, чем сберегательные счета.
    CREDIT_CARD, //Кредитная карта предоставляет клиенту кредитный лимит, который можно использовать для совершения покупок. Средства можно вернуть в банк в течение определенного срока без начисления процентов.
    DEBIT_CARD, // Дебетовая карта связана с текущим счетом клиента и позволяет снимать средства с него и совершать платежи, используя доступные средства на счете.

}
