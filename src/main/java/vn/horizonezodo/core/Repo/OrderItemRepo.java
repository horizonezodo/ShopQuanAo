package vn.horizonezodo.core.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.OrderItem;
import vn.horizonezodo.core.Entity.Orders;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    Boolean existsByOrOrderAndProductId(Orders orders, String productId);
    Optional<OrderItem> findByOrOrderAndProductId(Orders orders, String productId);
    List<OrderItem> findAllByOrder(Orders order);
}
