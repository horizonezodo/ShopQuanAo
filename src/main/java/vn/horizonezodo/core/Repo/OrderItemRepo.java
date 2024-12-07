package vn.horizonezodo.core.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.OrderItem;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    Boolean existsByOrOrderAndProductId(Long orderId, String productId);
    Optional<OrderItem> findByOrOrderAndProductId(Long orderId, String productId);
    List<OrderItem> findAllByOrder(Long orderId);
}
