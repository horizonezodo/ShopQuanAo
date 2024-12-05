package vn.horizonezodo.core.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Orders;


@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
}
