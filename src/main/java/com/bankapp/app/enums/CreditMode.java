package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Режим расчетов.
 * <p>
 * ----- English -------
 * <p>
 * Calculation mode.
 */
public enum CreditMode {
    /**
     * ----- Russian ------
     * <p>
     * Это регулярные равные платежи, которые выплачиваются в течение определенного периода времени, как правило,
     * с определенной периодичностью (например, ежемесячно или ежегодно).
     * В случае кредитов или займов с аннуитетными платежами, каждый платеж включает как основной долг, так и проценты,
     * и сумма платежа остается постоянной на протяжении всего срока.
     * <p>
     * ----- English -------
     * <p>
     * These are regular equal payments that are paid over a specified period of time, usually
     * with a certain frequency (for example, monthly or annually).
     * In the case of loans or borrowings with annuity payments, each payment includes both principal and interest,
     * and the payment amount remains constant throughout the entire period.
     */
    ANNUITY,
    /**
     * ----- Russian ------
     * <p>
     * Разносторонние платежи, наоборот, подразумевают изменяющиеся платежи в течение срока кредита или займа.
     * В начале срока проценты выплачиваются на основную сумму, и сумма платежа уменьшается с каждым платежом, так как основной долг уменьшается.
     * Такие кредиты могут быть более выгодными для заемщиков, так как они могут платить меньше в начале срока.
     * <p>
     * ----- English -------
     * <p>
     * Variable payments, on the contrary, imply changing payments during the term of the loan or loan.
     * At the beginning of the term, interest is paid on the principal amount, and the payment amount decreases
     * with each payment as the principal balance decreases. These loans can be more profitable
     * for borrowers since they can pay less at the beginning of the term.
     */
    DIFFERENTIATED
}
