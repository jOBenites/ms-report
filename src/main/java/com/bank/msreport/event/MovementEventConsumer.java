package com.bank.msreport.event;

import com.bank.msreport.model.MovementView;
import com.bank.msreport.repository.MovementViewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consumidor de eventos de movimientos registrados en cuentas, creditos y tarjetas.
 * Registra el movimiento en la vista local para generacion de reportes.
 */
@Component
@RequiredArgsConstructor
public class MovementEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(MovementEventConsumer.class);

    private final MovementViewRepository movementViewRepository;

    /**
     * Consume bank.movement.recorded y registra el movimiento en la vista local.
     *
     * @param payload datos del evento (movementId, productId, productType, movementType, amount)
     */
    @KafkaListener(topics = "bank.movement.recorded", groupId = "ms-report")
    public void onMovementRecorded(Map<String, Object> payload) {
        String movementId = (String) payload.get("movementId");
        String productId = (String) payload.get("productId");
        String productType = (String) payload.get("productType");
        String movementType = (String) payload.get("movementType");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        BigDecimal commission = payload.get("commission") != null
                ? new BigDecimal(payload.get("commission").toString()) : BigDecimal.ZERO;

        MovementView movement = new MovementView(movementId, productId, productType,
                movementType, amount, commission, LocalDateTime.now());
        movementViewRepository.save(movement)
                .subscribe(m -> log.info("Movimiento {} registrado en reportes", movementId));
    }
}
