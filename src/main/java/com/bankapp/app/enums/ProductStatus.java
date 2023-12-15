package com.bankapp.app.enums;

/**
 * ----- Russian ------
 * <p>
 * Статус продукта.
 * <p>
 * ----- English -------
 * <p>
 * Product status.
 */
public enum ProductStatus {
    /**
     * ----- Russian ------
     * <p>
     * Банковский продукт (например, счет, кредитная карта, депозит) активен и может быть использован клиентом в соответствии с условиями соглашения.
     * <p>
     * ----- English -------
     * <p>
     * The banking product (for example, account, credit card, deposit) is active and can be used by the client in accordance with the terms of the agreement.
     */
    ACTIVE,
    /**
     * ----- Russian ------
     * <p>
     * Банковский продукт был закрыт клиентом или банком, и его действие больше не активно. Это может произойти после закрытия счета, погашения кредита или других обстоятельств.
     * <p>
     * ----- English -------
     * <p>
     * The banking product has been closed by the client or the bank and is no longer active. This may occur after account closure, loan repayment, or other circumstances.
     */
    CLOSED,
    /**
     * ----- Russian ------
     * <p>
     * Этот статус может использоваться для продуктов с дополнительными опциями или услугами, которые клиент выбрал или активировал.
     * <p>
     * ----- English -------
     * <p>
     * This status can be used for products with additional options or services that the customer has selected or activated.
     */
    EXTENDED
}
