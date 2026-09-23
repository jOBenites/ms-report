package com.bank.msreport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Vista de lectura local de pagos entre monederos wallet, alimentada por
 * el evento bank.wallet.payment-sent. Permite reportar los pagos Yanki
 * sin consultar la base de ms-wallet (database-per-service).
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "wallet_payment_view")
public class WalletPaymentView {

    @Id
    private String paymentId;

    private String sourcePhoneNumber;

    private String targetPhoneNumber;

    private BigDecimal amount;

    private LocalDateTime occurredAt;

    /**
     * Constructor completo de la vista de pago wallet.
     *
     * @param paymentId identificador del pago
     * @param sourcePhoneNumber celular de origen
     * @param targetPhoneNumber celular de destino
     * @param amount monto del pago
     * @param occurredAt fecha y hora del pago
     */
    public WalletPaymentView(String paymentId, String sourcePhoneNumber, String targetPhoneNumber,
                             BigDecimal amount, LocalDateTime occurredAt) {
        this.paymentId = paymentId;
        this.sourcePhoneNumber = sourcePhoneNumber;
        this.targetPhoneNumber = targetPhoneNumber;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }
}
