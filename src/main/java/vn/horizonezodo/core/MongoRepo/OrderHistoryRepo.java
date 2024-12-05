package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.OrderHistory;

@Repository
public interface OrderHistoryRepo extends MongoRepository<OrderHistory, String> {
}
