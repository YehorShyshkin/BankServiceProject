package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Статус соглашения.
 * <p>
 * ----- English -------
 * <p>
 * Agreement status.
 */
public enum AgreementStatus {
    /**
     * ----- Russian ------
     * <p>
     * Соглашение активно и действует в соответствии с условиями, установленными в нем.
     * Клиент и банк выполняют свои обязательства согласно этому соглашению.
     * <p>
     * ----- English -------
     * <p>
     * The Agreement is active and valid in accordance with the terms and conditions set forth therein.
     * The client and the bank fulfill their obligations under this agreement.
     */
    ACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение не активно.
     * <p>
     * ----- English -------
     * <p>
     * Agreement is not active.
     */
    INACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение было завершено и больше не действует.
     * Это может произойти, например, после полного погашения кредита или завершения договора.
     * <p>
     * ----- English -------
     * <p>
     * The agreement has been terminated and is no longer valid.
     * This may happen, for example, after full repayment of the loan or completion of the contract.
     */
    CLOSED,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение находится под рассмотрением банка из-за различных причин, например, из-за неясных условий или подозрительных операций.
     * Расследование может быть временным.
     * <p>
     * ----- English -------
     * <p>
     * The agreement is under review by the bank for various reasons, such as unclear terms or suspicious transactions.
     * The investigation may be temporary.
     */
    UNDER_REVIEW,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение было отменено одной из сторон, и оно больше не действует.
     * Это может произойти по разным причинам, например, по запросу клиента или из-за нарушений условий соглашения.
     * <p>
     * ----- English -------
     * <p>
     * The agreement was canceled by one of the parties and is no longer valid.
     * This can happen for various reasons, such as a client request or due to violations of the terms of the agreement.
     */
    CANCELLED,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение было успешно выполнено и закрыто после выполнения всех его условий.
     * <p>
     * ----- English -------
     * <p>
     * The agreement was successfully executed and closed after all its conditions were met.
     */
    EXECUTED,
    /**
     * ----- Russian ------
     * <p>
     * Если клиент не выполнил свои обязательства в соответствии с соглашением, оно может быть помечено как просроченное.
     * Это может относиться к просрочке платежей по кредиту или другим обязательствам.
     * <p>
     * ----- English -------
     * <p>
     * If the client has not fulfilled its obligations under the agreement, it may be marked as overdue.
     * This may refer to late payments on a loan or other obligations.
     */
    OVERDUE,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение находится в процессе выполнения. Например, кредитное соглашение может быть в статусе "Исполняется",
     * пока клиент выплачивает задолженность.
     * <p>
     * ----- English -------
     * <p>
     * The agreement is in the process of being implemented.
     * For example, a loan agreement may be in the “In Progress” status while the client pays off the debt.
     */
    IN_PROGRESS,
    /**
     * ----- Russian ------
     * <p>
     * Соглашение временно приостановлено, и его действие приостановлено по какой-либо причине.
     * <p>
     * ----- English -------
     * <p>
     * The agreement is temporarily suspended and suspended for any reason.
     */
    SUSPENDED
}
