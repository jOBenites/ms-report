package com.bank.msreport.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para productos en reportes.
 */
@Getter
@Setter
public class ProductResponse {

    private String productId;
    private String customerId;
    private String productType;
    private String status;
    private LocalDateTime registeredAt;
}
