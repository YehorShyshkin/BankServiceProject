package com.bankapp.app.dto;

import lombok.*;

@Data
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
