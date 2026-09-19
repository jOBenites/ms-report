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
 * Consumidor de eventos de otorgamiento de creditos.
 * Registra el credito como producto en la vista local.
 */
@Component
@RequiredArgsConstructor
public class CreditEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CreditEventConsumer.class);
    private static final String PRODUCT_TYPE_CREDIT = "CREDIT";

    private final ProductViewRepository productViewRepository;

    /**
     * Consume bank.credit.granted y registra el producto en la vista local.
     *
     * @param payload datos del evento (creditId, customerId, creditType, amount)
     */
    @KafkaListener(topics = "bank.credit.granted", groupId = "ms-report")
    public void onCreditGranted(Map<String, Object> payload) {
        String creditId = (String) payload.get("creditId");
        String customerId = (String) payload.get("customerId");
        ProductView product = new ProductView(creditId, customerId, PRODUCT_TYPE_CREDIT,
                "ACTIVE", LocalDateTime.now());
        productViewRepository.save(product)
                .subscribe(p -> log.info("Producto credito {} registrado en reportes", creditId));
    }
}
