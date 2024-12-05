package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Color;

@Repository
public interface ColorRepo extends MongoRepository<Color,String> {
}
