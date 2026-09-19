package com.bank.msreport.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para movimientos en reportes.
 */
@Getter
@Setter
public class MovementResponse {

    private String movementId;
    private String productId;
    private String productType;
    private String movementType;
    private BigDecimal amount;
    private BigDecimal commission;
    private LocalDateTime occurredAt;
}
