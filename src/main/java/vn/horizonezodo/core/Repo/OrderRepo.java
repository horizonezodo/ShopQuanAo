package vn.horizonezodo.core.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.ORDERSTATUS;
import vn.horizonezodo.core.Entity.Orders;

import java.util.Optional;


@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
    Optional<Orders> findByUserAndOrderstatus(Long userId, ORDERSTATUS orderstatus);
}
