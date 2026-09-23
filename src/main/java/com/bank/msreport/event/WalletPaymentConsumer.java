package com.bank.msreport.event;

import com.bank.msreport.model.WalletPaymentView;
import com.bank.msreport.repository.WalletPaymentViewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consumidor del evento bank.wallet.payment-sent.
 * Registra el pago entre monederos en la vista local de reportes.
 */
@Component
@RequiredArgsConstructor
public class WalletPaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(WalletPaymentConsumer.class);

    private final WalletPaymentViewRepository walletPaymentViewRepository;

    /**
     * Consume bank.wallet.payment-sent y guarda el pago en la vista local.
     *
     * @param payload datos del evento (paymentId, sourcePhoneNumber, targetPhoneNumber, amount, occurredAt)
     */
    @KafkaListener(topics = "bank.wallet.payment-sent", groupId = "ms-report")
    public void onPaymentSent(Map<String, Object> payload) {
        String paymentId = (String) payload.get("paymentId");
        String sourcePhoneNumber = (String) payload.get("sourcePhoneNumber");
        String targetPhoneNumber = (String) payload.get("targetPhoneNumber");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        WalletPaymentView payment = new WalletPaymentView(paymentId, sourcePhoneNumber,
                targetPhoneNumber, amount, LocalDateTime.now());
        walletPaymentViewRepository.save(payment)
                .subscribe(p -> log.info("Pago wallet {} registrado en reportes", paymentId));
    }
}
