package com.bank.msreport.event;

import com.bank.msreport.cache.ProductViewCacheService;
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
 * Consumidor de eventos de apertura de cuentas.
 * Registra la cuenta como producto en la vista local y en caché Redis.
 */
@Component
@RequiredArgsConstructor
public class AccountEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AccountEventConsumer.class);
    private static final String PRODUCT_TYPE_ACCOUNT = "ACCOUNT";

    private final ProductViewRepository productViewRepository;
    private final ProductViewCacheService productViewCacheService;

    /**
     * Consume bank.account.opened y registra el producto en la vista local.
     * Actualiza tanto MongoDB como caché Redis.
     *
     * @param payload datos del evento (accountId, customerId, accountType)
     */
    @KafkaListener(topics = "bank.account.opened", groupId = "ms-report")
    public void onAccountOpened(Map<String, Object> payload) {
        String accountId = (String) payload.get("accountId");
        String customerId = (String) payload.get("customerId");
        ProductView product = new ProductView(accountId, customerId, PRODUCT_TYPE_ACCOUNT,
                "ACTIVE", LocalDateTime.now());
        productViewRepository.save(product)
                .flatMap(productViewCacheService::put)
                .subscribe(p -> log.info("Producto cuenta {} registrado en reportes (MongoDB + Redis)", accountId));
    }
}
