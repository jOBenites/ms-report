package com.bank.msreport.service;

import com.bank.msreport.cache.MovementViewCacheService;
import com.bank.msreport.cache.ProductViewCacheService;
import com.bank.msreport.dto.MovementResponse;
import com.bank.msreport.dto.ProductResponse;
import com.bank.msreport.model.MovementView;
import com.bank.msreport.model.ProductView;
import com.bank.msreport.repository.MovementViewRepository;
import com.bank.msreport.repository.ProductViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Servicio reactivo de reportes del sistema bancario.
 * Expone consultas de productos por cliente y tipo, y movimientos
 * por producto en rango de fechas o ultimos N registros.
 * Utiliza Redis como caché para reducir consultas a MongoDB.
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private static final int DEFAULT_MOVEMENT_LIMIT = 10;

    private final ProductViewRepository productViewRepository;
    private final MovementViewRepository movementViewRepository;
    private final ProductViewCacheService productViewCacheService;
    private final MovementViewCacheService movementViewCacheService;

    /**
     * Lista los productos de un cliente.
     *
     * @param customerId identificador del cliente
     * @return Flux con los productos
     */
    public Flux<ProductResponse> findProductsByCustomerId(String customerId) {
        return productViewRepository.findByCustomerId(customerId)
                .flatMap(product -> productViewCacheService.put(product).thenReturn(product))
                .map(this::toProductResponse);
    }

    /**
     * Lista los productos de un cliente por tipo.
     *
     * @param customerId identificador del cliente
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @return Flux con los productos
     */
    public Flux<ProductResponse> findProductsByCustomerAndType(String customerId, String productType) {
        return productViewRepository.findByCustomerIdAndProductType(customerId, productType)
                .flatMap(product -> productViewCacheService.put(product).thenReturn(product))
                .map(this::toProductResponse);
    }

    /**
     * Lista los ultimos 10 movimientos de un producto.
     * Intenta obtener el conteo de Redis primero para optimizar.
     *
     * @param productId identificador del producto
     * @return Flux con los movimientos
     */
    public Flux<MovementResponse> findLastMovements(String productId) {
        return movementViewRepository.findTopNByProductIdOrderByOccurredAtDesc(productId, DEFAULT_MOVEMENT_LIMIT)
                .flatMap(movement -> movementViewCacheService.putCount(productId, DEFAULT_MOVEMENT_LIMIT)
                        .thenReturn(movement))
                .map(this::toMovementResponse);
    }

    /**
     * Lista los movimientos de un producto en un rango de fechas.
     *
     * @param productId identificador del producto
     * @param start fecha inicio del rango
     * @param end fecha fin del rango
     * @return Flux con los movimientos
     */
    public Flux<MovementResponse> findMovementsByDateRange(String productId,
                                                            LocalDateTime start, LocalDateTime end) {
        return movementViewRepository.findByProductIdAndOccurredAtBetweenOrderByOccurredAtDesc(productId, start, end)
                .map(this::toMovementResponse);
    }

    /**
     * Convierte una entidad ProductView a su DTO de respuesta.
     *
     * @param product entidad a convertir
     * @return DTO con los campos poblados
     */
    ProductResponse toProductResponse(ProductView product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setCustomerId(product.getCustomerId());
        response.setProductType(product.getProductType());
        response.setStatus(product.getStatus());
        response.setRegisteredAt(product.getRegisteredAt());
        return response;
    }

    /**
     * Convierte una entidad MovementView a su DTO de respuesta.
     *
     * @param movement entidad a convertir
     * @return DTO con los campos poblados
     */
    MovementResponse toMovementResponse(MovementView movement) {
        MovementResponse response = new MovementResponse();
        response.setMovementId(movement.getMovementId());
        response.setProductId(movement.getProductId());
        response.setProductType(movement.getProductType());
        response.setMovementType(movement.getMovementType());
        response.setAmount(movement.getAmount());
        response.setCommission(movement.getCommission());
        response.setOccurredAt(movement.getOccurredAt());
        return response;
    }
}
