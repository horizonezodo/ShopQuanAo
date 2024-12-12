package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Size;

import java.util.List;

@Repository
public interface SizeRepo extends MongoRepository<Size, String> {
    List<Size> findAllByProductId(String productId);
}
