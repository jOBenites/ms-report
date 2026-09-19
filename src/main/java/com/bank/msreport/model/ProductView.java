package com.bank.msreport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Vista de lectura local de productos bancarios, alimentada por eventos
 * bank.account.opened, bank.credit.granted y bank.creditcard.issued.
 * Permite generar reportes por cliente y tipo de producto sin consultar
 * las bases de otros microservicios (database-per-service).
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "product_view")
@CompoundIndex(name = "customer_product", def = "{customerId: 1, productType: 1}")
public class ProductView {

    @Id
    private String productId;

    private String customerId;

    private String productType;

    private String status;

    private LocalDateTime registeredAt;

    /**
     * Constructor completo de la vista de producto.
     *
     * @param productId identificador del producto
     * @param customerId identificador del cliente titular
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @param status estado del producto (ACTIVE, PAID)
     * @param registeredAt fecha de registro del producto
     */
    public ProductView(String productId, String customerId, String productType,
                       String status, LocalDateTime registeredAt) {
        this.productId = productId;
        this.customerId = customerId;
        this.productType = productType;
        this.status = status;
        this.registeredAt = registeredAt;
    }
}
