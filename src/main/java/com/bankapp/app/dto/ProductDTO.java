package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDTO {
    /**
     * Product
     */
    private String productName;
    private String productStatus;
    private String currencyCode;
    private String productInterestRate;
    private String productLimit;

    /**
     * Manager
     */
    private String managerLastName;
    private String managerStatus;
}
