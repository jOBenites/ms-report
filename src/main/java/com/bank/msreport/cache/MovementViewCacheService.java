package com.bank.msreport.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Servicio de caché Redis para la vista de movimientos.
 * Almacena la cantidad de movimientos consultados por producto
 * para optimizar reportes de ultimos N movimientos.
 */
@Service
@RequiredArgsConstructor
public class MovementViewCacheService {

    private static final String KEY_PREFIX = "report:movement:count:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    /**
     * Obtiene el conteo de movimientos en caché para un producto.
     *
     * @param productId identificador del producto
     * @return Mono con el conteo o vacío si no está en caché
     */
    public Mono<Long> getCount(String productId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + productId)
                .filter(obj -> obj instanceof Long)
                .map(obj -> (Long) obj);
    }

    /**
     * Almacena el conteo de movimientos en caché.
     *
     * @param productId identificador del producto
     * @param count cantidad de movimientos
     * @return Mono<Void> completado cuando se almacena
     */
    public Mono<Void> putCount(String productId, long count) {
        return redisTemplate.opsForValue()
                .set(KEY_PREFIX + productId, count, TTL)
                .then();
    }

    /**
     * Elimina el conteo de movimientos de caché.
     *
     * @param productId identificador del producto
     * @return Mono<Long> con el número de claves eliminadas
     */
    public Mono<Long> evict(String productId) {
        return redisTemplate.delete(KEY_PREFIX + productId);
    }
}
