package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Тип счета.
 * <p>
 * ----- English -------
 * <p>
 * Account type.
 */
public enum AccountType {
    /**
     * ----- Russian ------
     * <p>
     * Текущий счет предназначен для повседневных финансовых операций.
     * С него можно совершать платежи, писать чеки, снимать деньги и делать переводы.
     * На таких счетах часто не начисляются или начисляются небольшие проценты.
     * <p>
     * ----- English -------
     * <p>
     * A checking account is designed for day-to-day financial transactions.
     * You can use it to make payments, write checks, withdraw money and make transfers.
     * These accounts often accrue little or no interest.
     */
    CHECKING_ACCOUNT,
    /**
     * ----- Russian ------
     * <p>
     * Депозитный счет - это счет, на который клиент вносит сумму денег на фиксированный срок с фиксированной процентной ставкой.
     * В обмен на это, клиент обязуется не снимать средства с счета до окончания срока.
     * Депозиты обычно предоставляют более высокие проценты, чем сберегательные счета.
     * <p>
     * ----- English -------
     * <p>
     * A deposit account is an account into which a client deposits a sum of money for a fixed period at a fixed interest rate.
     * In exchange for this, the client agrees not to withdraw funds from the account before the end of the term.
     * Deposits typically provide higher interest rates than savings accounts.
     */
    CERTIFICATE_OF_DEPOSIT,
    /**
     * ----- Russian ------
     * <p>
     * Кредитная карта предоставляет клиенту кредитный лимит, который можно использовать для совершения покупок.
     * Средства можно вернуть в банк в течение определенного срока без начисления процентов.
     * <p>
     * ----- English -------
     * <p>
     * A credit card provides the customer with a credit limit that can be used to make purchases.
     * Funds can be returned to the bank within a certain period without interest.
     */
    CREDIT_CARD,
    /**
     * ----- Russian ------
     * <p>
     * Дебетовая карта связана с текущим счетом клиента и позволяет снимать средства с него и совершать платежи,
     * используя доступные средства на счете.
     * <p>
     * ----- English -------
     * <p>
     * A debit card is linked to a customer's checking account and allows them to withdraw funds from the account and
     * make payments using available funds in the account.
     */
    DEBIT_CARD
}
