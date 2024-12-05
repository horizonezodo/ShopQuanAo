package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Size;

@Repository
public interface SizeRepo extends MongoRepository<Size, String> {
}
