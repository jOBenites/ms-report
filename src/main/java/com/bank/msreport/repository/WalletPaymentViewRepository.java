package com.bank.msreport.repository;

import com.bank.msreport.model.WalletPaymentView;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Repositorio reactivo para la vista de pagos wallet.
 * Se alimenta exclusivamente del evento bank.wallet.payment-sent.
 */
public interface WalletPaymentViewRepository extends ReactiveMongoRepository<WalletPaymentView, String> {
}
