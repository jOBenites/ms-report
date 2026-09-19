package com.bank.msreport.event;

import com.bank.msreport.model.ProductView;
import com.bank.msreport.repository.ProductViewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consumidor de eventos de emision de tarjetas de credito.
 * Registra la tarjeta como producto en la vista local.
 */
@Component
@RequiredArgsConstructor
public class CreditCardEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CreditCardEventConsumer.class);
    private static final String PRODUCT_TYPE_CREDIT_CARD = "CREDIT_CARD";

    private final ProductViewRepository productViewRepository;

    /**
     * Consume bank.creditcard.issued y registra el producto en la vista local.
     *
     * @param payload datos del evento (cardId, customerId, cardType, creditLimit)
     */
    @KafkaListener(topics = "bank.creditcard.issued", groupId = "ms-report")
    public void onCreditCardIssued(Map<String, Object> payload) {
        String cardId = (String) payload.get("cardId");
        String customerId = (String) payload.get("customerId");
        ProductView product = new ProductView(cardId, customerId, PRODUCT_TYPE_CREDIT_CARD,
                "ACTIVE", LocalDateTime.now());
        productViewRepository.save(product)
                .subscribe(p -> log.info("Producto tarjeta de credito {} registrado en reportes", cardId));
    }
}
