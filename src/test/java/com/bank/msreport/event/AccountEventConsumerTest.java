package com.bank.msreport.event;

import com.bank.msreport.model.ProductView;
import com.bank.msreport.repository.ProductViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link AccountEventConsumer}.
 * Valida el registro de cuentas en la vista local de reportes.
 */
@ExtendWith(MockitoExtension.class)
class AccountEventConsumerTest {

    @Mock
    private ProductViewRepository productViewRepository;

    @InjectMocks
    private AccountEventConsumer accountEventConsumer;

    @Test
    void onAccountOpened_savesProductView() {
        when(productViewRepository.save(any(ProductView.class))).thenReturn(Mono.just(new ProductView()));

        Map<String, Object> payload = Map.of(
                "accountId", "acc-1",
                "customerId", "cust-1",
                "accountType", "SAVINGS"
        );
        accountEventConsumer.onAccountOpened(payload);

        ArgumentCaptor<ProductView> captor = ArgumentCaptor.forClass(ProductView.class);
        verify(productViewRepository).save(captor.capture());
        assertEquals("acc-1", captor.getValue().getProductId());
        assertEquals("cust-1", captor.getValue().getCustomerId());
        assertEquals("ACCOUNT", captor.getValue().getProductType());
    }
}
