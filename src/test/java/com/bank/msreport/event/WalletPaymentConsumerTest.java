package com.bank.msreport.event;

import com.bank.msreport.model.WalletPaymentView;
import com.bank.msreport.repository.WalletPaymentViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para {@link WalletPaymentConsumer}.
 * Valida el registro de pagos wallet en la vista local de reportes.
 */
@ExtendWith(MockitoExtension.class)
class WalletPaymentConsumerTest {

    @Mock
    private WalletPaymentViewRepository walletPaymentViewRepository;

    @InjectMocks
    private WalletPaymentConsumer walletPaymentConsumer;

    @Test
    void onPaymentSent_savesWalletPaymentView() {
        when(walletPaymentViewRepository.save(any(WalletPaymentView.class)))
                .thenReturn(Mono.just(new WalletPaymentView()));

        walletPaymentConsumer.onPaymentSent(Map.of(
                "paymentId", "pay-1",
                "sourcePhoneNumber", "+5491123456789",
                "targetPhoneNumber", "+5491123456790",
                "amount", new BigDecimal("25.00"),
                "occurredAt", "2026-09-23T17:35:59"
        ));

        ArgumentCaptor<WalletPaymentView> captor = ArgumentCaptor.forClass(WalletPaymentView.class);
        verify(walletPaymentViewRepository).save(captor.capture());
        assertEquals("pay-1", captor.getValue().getPaymentId());
        assertEquals("+5491123456789", captor.getValue().getSourcePhoneNumber());
        assertEquals("+5491123456790", captor.getValue().getTargetPhoneNumber());
        assertEquals(new BigDecimal("25.00"), captor.getValue().getAmount());
    }
}
