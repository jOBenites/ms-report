package com.bank.msreport.repository;

import com.bank.msreport.model.MovementView;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Repositorio reactivo para la entidad MovementView en MongoDB.
 * No se permite @Query ni consultas dinamicas segun las reglas del proyecto.
 */
public interface MovementViewRepository extends ReactiveMongoRepository<MovementView, String> {

    /**
     * Busca los movimientos de un producto en un rango de fechas,
     * ordenados del mas reciente al mas antiguo.
     *
     * @param productId identificador del producto
     * @param start fecha inicio del rango
     * @param end fecha fin del rango
     * @return Flux con los movimientos encontrados
     */
    Flux<MovementView> findByProductIdAndOccurredAtBetweenOrderByOccurredAtDesc(
            String productId, LocalDateTime start, LocalDateTime end);

    /**
     * Busca los ultimos N movimientos de un producto.
     *
     * @param productId identificador del producto
     * @param limit cantidad maxima de movimientos a retornar
     * @return Flux con los movimientos encontrados
     */
    Flux<MovementView> findTopNByProductIdOrderByOccurredAtDesc(String productId, int limit);
}
