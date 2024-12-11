package vn.horizonezodo.core.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.ORDERSTATUS;
import vn.horizonezodo.core.Entity.Orders;
import vn.horizonezodo.core.Entity.User;

import java.util.Optional;


@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
    Optional<Orders> findByUserAndOrderstatus(User user, ORDERSTATUS orderstatus);
    Page<Orders> findAllByUserAndOrderstatus(User user, ORDERSTATUS orderstatus, Pageable pageable);
//    Optional<Orders> findByIdAndOrderStatus(Long orderId, ORDERSTATUS orderstatus);
}
