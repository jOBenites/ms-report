package com.bank.msreport.repository;

import com.bank.msreport.model.ProductView;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo para la entidad ProductView en MongoDB.
 * No se permite @Query ni consultas dinamicas segun las reglas del proyecto.
 */
public interface ProductViewRepository extends ReactiveMongoRepository<ProductView, String> {

    /**
     * Busca los productos de un cliente por tipo.
     *
     * @param customerId identificador del cliente
     * @param productType tipo de producto (ACCOUNT, CREDIT, CREDIT_CARD)
     * @return Flux con los productos encontrados
     */
    Flux<ProductView> findByCustomerIdAndProductType(String customerId, String productType);

    /**
     * Busca todos los productos de un cliente.
     *
     * @param customerId identificador del cliente
     * @return Flux con los productos encontrados
     */
    Flux<ProductView> findByCustomerId(String customerId);
}
