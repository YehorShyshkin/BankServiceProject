package com.bankapp.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerDTO {

    /**
     * Manager
     */
    private String managerLastName;
    private String managerStatus;

}
