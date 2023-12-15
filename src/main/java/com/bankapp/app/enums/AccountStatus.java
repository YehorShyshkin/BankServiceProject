package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Статус соглашения
 * <p>
 * ----- English -------
 * <p>
 * Agreement status
 */
public enum AccountStatus {
    /**
     * ----- Russian ------
     * <p>
     * Это стандартный статус счета, который указывает на то, что счет активен и его можно использовать для проведения операций,
     * таких как депозиты, снятия, переводы и т. д.
     * <p>
     * ----- English -------
     * <p>
     * This is the standard account status which indicates that the account is active and can be used for transactions
     * such as deposits, withdrawals, transfers, etc.
     */
    ACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Счет или аккаунт в банке находится в неактивном состоянии или был деактивирован.
     * <p>
     * ----- English -------
     * <p>
     * The bank account or account is inactive or has been deactivated.
     */
    INACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Этот статус означает, что счет был закрыт по различным причинам, например, по запросу клиента или из-за неактивности.
     * После закрытия счета на него нельзя будет проводить операции.
     * <p>
     * ---- English -------
     * <p>
     * This status means that the account has been closed for various reasons, such as a customer request or due to inactivity.
     * Once the account is closed, no transactions can be made on it.
     */
    CLOSED,
    /**
     * ----- Russian ------
     * <p>
     * Может быть заморожен банком из-за различных финансовых или юридических проблем.
     * В этом случае клиент временно не может проводить операции на счете, пока счет не будет разморожен.
     * <p>
     * ---- English -------
     * <p>
     * may be frozen by the bank due to various financial or legal problems.
     * In this case, the client is temporarily unable to conduct transactions on the account until the account is unfrozen.
     */
    FROZEN,
    /**
     * ----- Russian ------
     * <p>
     * Если баланс на счете равен нулю, это может быть отмечено как нулевой баланс.
     * Это может быть временным статусом перед закрытием счета или после снятия всех средств.
     * <p>
     * ---- English -------
     * <p>
     * If the account balance is zero, it may be noted as a zero balance.
     * This may be a temporary status before the account is closed or after all funds have been withdrawn.
     */
    ZERO_BALANCE,
    /**
     * ----- Russian ------
     * <p>
     * Этот статус может быть применен к счету, если счет был арестован правоохранительными органами или в результате судебных действий.
     * <p>
     * ---- English -------
     * <p>
     * This status may be applied to an account if the account has been seized by law enforcement or as a result of legal action.
     */
    SEIZED,
    /**
     * ----- Russian ------
     * <p>
     * Счет может быть приостановлен из-за различных причин, таких как обнаружение мошенничества, нарушение условий соглашения и другие.
     * <p>
     * ---- English -------
     * <p>
     * An account may be suspended due to various reasons such as detection of fraud, violation of terms of agreement and others.
     */
    SUSPENDED,
    /**
     * ----- Russian ------
     * <p>
     * Если счет подвергается расследованию или проверке, его статус может быть изменен на подозрительный до завершения расследования.
     * <p>
     * ---- English -------
     * <p>
     * If an account is subject to investigation or audit, its status may be changed to suspicious pending the completion of the investigation.
     */
    UNDER_INVESTIGATE
}
