package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Статус клиента.
 * <p>
 * ----- English -------
 * <p>
 * Client status.
 */
public enum ClientStatus {
    /**
     * ----- Russian ------
     * <p>
     * Этот статус обозначает, что клиент активен и имеет доступ к банковским услугам. Он может проводить операции,
     * открывать счета и использовать другие банковские услуги.
     * <p>
     * ----- English -------
     * This status means that the client is active and has access to banking services. He can conduct transactions,
     * open accounts and use other banking services.
     * <p>
     *
     */
    ACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Этот статус обозначает что клиентам, которые имеют высокий баланс средств на счете или активно используют банковские услуги.
     * Премиум-клиенты могут иметь дополнительные привилегии и предложения.
     * <p>
     * ----- English -------
     * <p>
     * This status means that clients who have a high account balance or actively use banking services.
     * Premium customers may have additional benefits and offers.
     *
     */
    PREMIUM,
    /**
     * ----- Russian ------
     * <p>
     * Если клиент нарушил условия соглашения с банком или у него есть задолженность перед банком, его статус может быть заблокированным.
     * В этом случае клиент может быть ограничен в доступе к счетам и услугам.
     * <p>
     * ----- English -------
     * <p>
     * If the client has violated the terms of the agreement with the bank or has a debt to the bank, his status may be blocked.
     * In this case, the client may be limited in access to accounts and services.
     *
     */
    BLOCKED,
    /**
     * ----- Russian ------
     * <p>
     * Банк может установить статус "на рассмотрении" для клиентов, чьи счета или операции находятся под проверкой из-за
     * подозрительной активности или других причин.
     * <p>
     * ----- English -------
     * <p>
     * The bank may set a "pending" status for customers whose accounts or transactions are under review due
     * to suspicious activity or other reasons.
     */
    UNDER_REVIEW,
    /**
     * ----- Russian ------
     * <p>
     * Когда клиент решает закрыть свою учетную запись в банке, статус становится "закрытым".
     * Также банк может закрыть счет по различным причинам.
     * <p>
     * ----- English -------
     * <p>
     * When a customer decides to close their bank account, the status becomes "closed".
     * Also, the bank may close the account for various reasons.
     */
    CLOSED,
    /**
     * ----- Russian ------
     * <p>
     * Если клиент не выполнил обязательства перед банком, например, не выплатил кредит вовремя, его статус может стать "просроченным".
     * <p>
     * ----- English -------
     * <p>
     * If the client does not fulfill his obligations to the bank, for example, does not repay the loan on time, his status may become “overdue.”
     */
    DELINQUENT,
    /**
     * ----- Russian ------
     * <p>
     * В некоторых случаях банк может определить клиента как "высокий риск", если у него есть признаки финансовой неустойчивости или риска для банка.
     * <p>
     * ----- English -------
     * <p>
     * In some cases, a bank may designate a customer as "high risk" if they show signs of financial instability or a risk to the bank.
     */
    HIGH_RISK
}
