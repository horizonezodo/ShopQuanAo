package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Color;

import java.util.List;

@Repository
public interface ColorRepo extends MongoRepository<Color,String> {
    List<Color> findAllByProductId(String productId);
}
