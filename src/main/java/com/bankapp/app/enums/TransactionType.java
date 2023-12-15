package com.bankapp.app.enums;
/**
 * ----- Russian ------
 * <p>
 * Тип транзакций.
 * <p>
 * ----- English -------
 * <p>
 * Transaction type.
 */
public enum TransactionType {
    /**
     * ----- Russian ------
     * <p>
     * Внесение средств на счет.
     * <p>
     * ----- English -------
     * <p>
     * Depositing funds into the account.
     */
    DEPOSIT,
    /**
     * ----- Russian ------
     * <p>
     * Снятие средств со счета.
     * <p>
     * ----- English -------
     * <p>
     * Withdrawal of funds from the account.
     */
    WITHDRAWAL,
    /**
     * ----- Russian ------
     * <p>
     * Перевод между счетами.
     * <p>
     * ----- English -------
     * <p>
     * Transfer between accounts.
     */
    TRANSFER,
    /**
     * ----- Russian ------
     * <p>
     * Платеж.
     * <p>
     * ----- English -------
     * <p>
     * Payment.
     */
    PAYMENT,
    /**
     * ----- Russian ------
     * <p>
     * Возврат денег.
     * <p>
     * ----- English -------
     * <p>
     * Refund.
     */
    REFUND
}
