package com.bank.msreport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Vista de lectura local de movimientos, alimentada por el evento
 * bank.movement.recorded. Permite consultar movimientos por producto
 * y rango de fechas sin consultar la base de otros microservicios.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "movement_view")
@CompoundIndex(name = "product_occurred", def = "{productId: 1, occurredAt: -1}")
public class MovementView {

    @Id
    private String movementId;

    private String productId;

    private String productType;

    private String movementType;

    private BigDecimal amount;

    private BigDecimal commission;

    private LocalDateTime occurredAt;

    /**
     * Constructor completo de la vista de movimiento.
     *
     * @param movementId identificador del movimiento
     * @param productId identificador del producto afectado
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @param movementType tipo de movimiento (DEPOSIT, WITHDRAWAL, etc.)
     * @param amount monto del movimiento
     * @param commission comision cobrada
     * @param occurredAt fecha y hora del movimiento
     */
    public MovementView(String movementId, String productId, String productType,
                        String movementType, BigDecimal amount, BigDecimal commission,
                        LocalDateTime occurredAt) {
        this.movementId = movementId;
        this.productId = productId;
        this.productType = productType;
        this.movementType = movementType;
        this.amount = amount;
        this.commission = commission;
        this.occurredAt = occurredAt;
    }
}
