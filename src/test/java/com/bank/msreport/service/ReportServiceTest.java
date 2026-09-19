package com.bank.msreport.service;

import com.bank.msreport.dto.MovementResponse;
import com.bank.msreport.dto.ProductResponse;
import com.bank.msreport.model.MovementView;
import com.bank.msreport.model.ProductView;
import com.bank.msreport.repository.MovementViewRepository;
import com.bank.msreport.repository.ProductViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link ReportService}.
 * Valida las consultas de productos y movimientos para reportes.
 */
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ProductViewRepository productViewRepository;

    @Mock
    private MovementViewRepository movementViewRepository;

    @InjectMocks
    private ReportService reportService;

    private ProductView productView;
    private MovementView movementView;

    @BeforeEach
    void setUp() {
        productView = new ProductView("prod-1", "cust-1", "ACCOUNT", "ACTIVE", LocalDateTime.now());
        movementView = new MovementView("mov-1", "prod-1", "ACCOUNT", "DEPOSIT",
                new BigDecimal("500.00"), BigDecimal.ZERO, LocalDateTime.now());
    }

    @Test
    void findProductsByCustomerId_success() {
        when(productViewRepository.findByCustomerId("cust-1")).thenReturn(Flux.just(productView));

        StepVerifier.create(reportService.findProductsByCustomerId("cust-1"))
                .assertNext(result -> {
                    assertEquals("prod-1", result.getProductId());
                    assertEquals("cust-1", result.getCustomerId());
                    assertEquals("ACCOUNT", result.getProductType());
                })
                .verifyComplete();
    }

    @Test
    void findProductsByCustomerAndType_success() {
        when(productViewRepository.findByCustomerIdAndProductType("cust-1", "ACCOUNT"))
                .thenReturn(Flux.just(productView));

        StepVerifier.create(reportService.findProductsByCustomerAndType("cust-1", "ACCOUNT"))
                .assertNext(result -> assertEquals("ACCOUNT", result.getProductType()))
                .verifyComplete();
    }

    @Test
    void findLastMovements_success() {
        when(movementViewRepository.findTopNByProductIdOrderByOccurredAtDesc("prod-1", 10))
                .thenReturn(Flux.just(movementView));

        StepVerifier.create(reportService.findLastMovements("prod-1"))
                .assertNext(result -> {
                    assertEquals("mov-1", result.getMovementId());
                    assertEquals(new BigDecimal("500.00"), result.getAmount());
                })
                .verifyComplete();
    }

    @Test
    void findMovementsByDateRange_success() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(movementViewRepository.findByProductIdAndOccurredAtBetweenOrderByOccurredAtDesc(
                "prod-1", start, end)).thenReturn(Flux.just(movementView));

        StepVerifier.create(reportService.findMovementsByDateRange("prod-1", start, end))
                .assertNext(result -> assertEquals("mov-1", result.getMovementId()))
                .verifyComplete();
    }

    @Test
    void toProductResponse_mapsAllFields() {
        ProductResponse response = reportService.toProductResponse(productView);

        assertNotNull(response);
        assertEquals("prod-1", response.getProductId());
        assertEquals("cust-1", response.getCustomerId());
        assertEquals("ACCOUNT", response.getProductType());
        assertEquals("ACTIVE", response.getStatus());
        assertNotNull(response.getRegisteredAt());
    }

    @Test
    void toMovementResponse_mapsAllFields() {
        MovementResponse response = reportService.toMovementResponse(movementView);

        assertNotNull(response);
        assertEquals("mov-1", response.getMovementId());
        assertEquals("prod-1", response.getProductId());
        assertEquals("ACCOUNT", response.getProductType());
        assertEquals("DEPOSIT", response.getMovementType());
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        assertEquals(BigDecimal.ZERO, response.getCommission());
        assertNotNull(response.getOccurredAt());
    }
}
