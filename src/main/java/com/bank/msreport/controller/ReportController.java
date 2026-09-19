package com.bank.msreport.controller;

import com.bank.msreport.dto.MovementResponse;
import com.bank.msreport.dto.ProductResponse;
import com.bank.msreport.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Controlador REST reactivo para reportes del sistema bancario.
 * Expone consultas de productos por cliente y movimientos por producto.
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Lista los productos de un cliente.
     *
     * @param customerId identificador del cliente
     * @return Flux con los productos
     */
    @GetMapping("/customers/{customerId}/products")
    public Flux<ProductResponse> getProductsByCustomerId(@PathVariable String customerId) {
        return reportService.findProductsByCustomerId(customerId);
    }

    /**
     * Lista los productos de un cliente por tipo.
     *
     * @param customerId identificador del cliente
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @return Flux con los productos
     */
    @GetMapping("/customers/{customerId}/products/{productType}")
    public Flux<ProductResponse> getProductsByCustomerAndType(
            @PathVariable String customerId,
            @PathVariable String productType) {
        return reportService.findProductsByCustomerAndType(customerId, productType);
    }

    /**
     * Lista los ultimos 10 movimientos de un producto.
     *
     * @param productId identificador del producto
     * @return Flux con los movimientos
     */
    @GetMapping("/products/{productId}/movements")
    public Flux<MovementResponse> getLastMovements(@PathVariable String productId) {
        return reportService.findLastMovements(productId);
    }

    /**
     * Lista los movimientos de un producto en un rango de fechas.
     *
     * @param productId identificador del producto
     * @param start fecha inicio del rango
     * @param end fecha fin del rango
     * @return Flux con los movimientos
     */
    @GetMapping("/products/{productId}/movements/range")
    public Flux<MovementResponse> getMovementsByDateRange(
            @PathVariable String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return reportService.findMovementsByDateRange(productId, start, end);
    }
}
