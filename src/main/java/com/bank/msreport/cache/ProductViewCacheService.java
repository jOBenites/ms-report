package com.bank.msreport.cache;

import com.bank.msreport.model.ProductView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Servicio de caché Redis para la vista de productos.
 * Almacena ProductView en Redis con TTL de 15 minutos
 * para reducir consultas a MongoDB en reportes frecuentes.
 */
@Service
@RequiredArgsConstructor
public class ProductViewCacheService {

    private static final String KEY_PREFIX = "report:product:";
    private static final String CUSTOMER_PRODUCTS_PREFIX = "report:customer-products:";
    private static final Duration TTL = Duration.ofMinutes(15);

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    /**
     * Busca un ProductView en caché por productId.
     *
     * @param productId identificador del producto
     * @return Mono con el ProductView o vacío si no está en caché
     */
    public Mono<ProductView> get(String productId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + productId)
                .filter(obj -> obj instanceof ProductView)
                .map(obj -> (ProductView) obj);
    }

    /**
     * Almacena un ProductView en caché.
     *
     * @param productView vista a almacenar
     * @return Mono<Void> completado cuando se almacena
     */
    public Mono<Void> put(ProductView productView) {
        return redisTemplate.opsForValue()
                .set(KEY_PREFIX + productView.getProductId(), productView, TTL)
                .then();
    }

    /**
     * Elimina un ProductView de caché.
     *
     * @param productId identificador del producto
     * @return Mono<Long> con el número de claves eliminadas
     */
    public Mono<Long> evict(String productId) {
        return redisTemplate.delete(KEY_PREFIX + productId);
    }
}
